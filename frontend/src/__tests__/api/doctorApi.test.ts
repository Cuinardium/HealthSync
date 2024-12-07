import {
  getDoctors,
  createDoctor,
  getDoctorById,
  updateDoctor,
  getDoctorAttendingHours,
  updateDoctorAttendingHours,
  getDoctorOccupiedHours,
} from "../../api/doctor/doctorApi";
import { axios } from "../../api/axios";
import {parseLocalDate} from "../../api/util/dateUtils";
import {AttendingHours} from "../../api/doctor/Doctor";

jest.mock("../../api/axios");

const mock = axios as jest.Mocked<typeof axios>;

// Mock the response data
const mockDoctorResponse = {
  id: 1,
  firstName: "John",
  lastName: "Doe",
  email: "john.doe@example.com",
  address: "123 Main St",
  city: "Cityville",
  locale: "en",
  rating: 4.5,
  ratingCount: 100,
  specialty: "Cardiology",
  healthInsurances: ["Insurance A", "Insurance B"],
  links: [],
};

const mockAttendingHoursResponse = [
  { day: "Monday", hours: ["09:00", "13:00"] },
  { day: "Tuesday", hours: ["14:00", "18:00"] },
];

const mockOccupiedHoursResponse = [
  { date: "2024-12-01", hours: ["09:00", "10:00"] },
  { date: "2024-12-02", hours: ["14:00", "15:00"] },
];

describe("Doctor API tests", () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should fetch a list of doctors", async () => {
    const mockQuery = { page: 1, pageSize: 10 };
    const mockGetDoctorsResponse = {
      status: 200,
      data: [mockDoctorResponse],
      headers: { link: 'rel="last" page=1' },
    };
    mock.get.mockResolvedValue(mockGetDoctorsResponse);

    const response = await getDoctors(mockQuery);

    expect(response.content).toHaveLength(1);
    expect(response.content).toBe(mockGetDoctorsResponse.data);
    expect(response.totalPages).toBe(1);
    expect(response.content[0].firstName).toBe(mockDoctorResponse.firstName);
  });

  it("should create a new doctor", async () => {
    const mockDoctorRegisterForm = {
      password: "password123",
      confirmPassword: "password123",
      name: "Jane",
      lastname: "Doe",
      email: "jane.doe@example.com",
      healthInsurances: ["Insurance A"],
      city: "New York",
      address: "456 Broadway",
      specialty: "Dermatology",
    };

    mock.post.mockResolvedValue({ status: 201 });

    await createDoctor(mockDoctorRegisterForm);

    expect(mock.post).toHaveBeenCalledWith(
      "/doctors",
      mockDoctorRegisterForm,
      expect.any(Object),
    );
  });

  it("should fetch a doctor by ID", async () => {
    const doctorId = "1";
    mock.get.mockResolvedValue({ data: mockDoctorResponse });

    const response = await getDoctorById(doctorId);

    expect(response.id).toBe(1);
    expect(response.firstName).toBe(mockDoctorResponse.firstName);
    expect(response.lastName).toBe(mockDoctorResponse.lastName);
  });

  it("should update a doctor", async () => {
    const doctorId = "1";
    const doctorEditForm = {
      name: "John",
      lastname: "Doe",
      healthInsurances: ["Insurance A", "Insurance B"],
      city: "Cityville",
      address: "123 Main St",
      specialty: "Cardiology",
      locale: "en",
    };

    mock.put.mockResolvedValue({ status: 200 });

    await updateDoctor(doctorId, doctorEditForm);

    expect(mock.put).toHaveBeenCalledWith(
      `/doctors/${doctorId}`,
      expect.any(FormData),
      { headers: { "Content-Type": "multipart/form-data" } },
    );
  });

  it("should fetch attending hours for a doctor", async () => {
    const doctorId = "1";
    mock.get.mockResolvedValue({ data: mockAttendingHoursResponse });

    const response = await getDoctorAttendingHours(doctorId);

    expect(response).toEqual(mockAttendingHoursResponse);
  });

  it("should update attending hours for a doctor", async () => {
    const doctorId = "1";
    const attendingHours: AttendingHours[] = [
      { day: "MONDAY", hours: ["09:00", "13:00"] },
      { day: "TUESDAY", hours: ["14:00", "18:00"] },
    ];

    mock.put.mockResolvedValue({ status: 200 });

    const response = await updateDoctorAttendingHours(doctorId, attendingHours);

    expect(response).toEqual(attendingHours);
  });

  it("should fetch occupied hours for a doctor", async () => {
    const doctorId = "1";
    const from = parseLocalDate("2024-12-01");
    const to = parseLocalDate("2024-12-31");
    mock.get.mockResolvedValue({
      data: mockOccupiedHoursResponse,
      status: 200,
      headers: { link: 'rel="last" page=1' },
    });

    const response = await getDoctorOccupiedHours(doctorId, from, to);

    expect(response).toEqual([
      { date: parseLocalDate("2024-12-01"), hours: ["09:00", "10:00"] },
      { date: parseLocalDate("2024-12-02"), hours: ["14:00", "15:00"] },
    ]);
  });
});
