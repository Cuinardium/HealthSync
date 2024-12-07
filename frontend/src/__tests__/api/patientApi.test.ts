import { axios } from "../../api/axios";
import {
  createPatient,
  getPatient,
  updatePatient,
} from "../../api/patient/patientApi";

jest.mock("../../api/axios");

const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("Patient API Tests", () => {
  const patientId = "123";
  const patientData = {
    name: "John",
    lastname: "Doe",
    email: "john.doe@example.com",
    healthInsurance: "HealthPlanX",
    locale: "en",
    password: "password123",
    confirmPassword: "password123",
  };

  const patientEditData = {
    name: "John",
    lastname: "Doe",
    healthInsurance: "HealthPlanX",
    locale: "en",
    image: new File(["image-content"], "profile.jpg", { type: "image/jpeg" }),
  };

  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("should create a new patient", async () => {
    mockedAxios.post.mockResolvedValue({ status: 201 });

    await createPatient(patientData);

    expect(mockedAxios.post).toHaveBeenCalledWith("/patients", patientData, {
      headers: { "Content-Type": "application/vnd.patient.v1+json" },
    });
  });

  it("should retrieve a patient by ID", async () => {
    const mockResponse = {
      data: {
        id: 123,
        firstName: "John",
        lastName: "Doe",
        email: "john.doe@example.com",
        locale: "en",
        links: [
          { rel: "self", href: "/patients/123", method: "GET" },
          {
            rel: "appointments",
            href: "/appointments?patientId=123",
            method: "GET",
          },
        ],
      },
    };

    mockedAxios.get.mockResolvedValue(mockResponse);

    const patient = await getPatient(patientId);

    expect(patient.id).toBe(123);
    expect(patient.firstName).toBe("John");
    expect(patient.lastName).toBe("Doe");
    expect(patient.email).toBe("john.doe@example.com");
    expect(patient.locale).toBe("en");
  });

  it("should update an existing patient", async () => {
    const mockPutResponse = { status: 200 };
    mockedAxios.put.mockResolvedValue(mockPutResponse);

    const updatedPatient = await updatePatient(patientId, patientEditData);

    expect(axios.put).toHaveBeenCalledWith(
      `/patients/${patientId}`,
      expect.any(FormData),
      { headers: { "Content-Type": "multipart/form-data" } },
    );

    // FormData cannot be directly tested like objects, but you can check if the method was called with FormData
    expect(updatedPatient.name).toBe("John");
    expect(updatedPatient.lastname).toBe("Doe");
    expect(updatedPatient.healthInsurance).toBe("HealthPlanX");
    expect(updatedPatient.locale).toBe("en");
  });
});
