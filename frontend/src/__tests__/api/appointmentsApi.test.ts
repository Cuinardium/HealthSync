import { axios } from "../../api/axios";
import {
  AppointmentForm,
  AppointmentQuery,
  AppointmentResponse,
  DoctorNotAvailable,
  PatientNotAvailable,
} from "../../api/appointment/Appointment";
import {
  cancelAppointment,
  createAppointment,
  getAllConfirmedAppointmentsInRange,
  getAppointment,
  getAppointments,
} from "../../api/appointment/appointmentsApi";
import { parseLocalDate } from "../../api/util/dateUtils";

jest.mock("../../api/axios");

const mockedAxios = axios as jest.Mocked<typeof axios>;

// Define some mocked data to reuse in the tests
const mockAppointment: AppointmentResponse = {
  id: 1,
  date: "2024-12-10",
  description: "Test appointment",
  cancelDescription: null,
  status: "CONFIRMED",
  timeBlock: "10:00",
  links: [
    { rel: "doctor", href: "/doctors/1", method: "GET" },
    { rel: "patient", href: "/patients/1", method: "GET" },
    { rel: "add-indication", href: "/indications/1", method: "POST" },
    { rel: "update-self", href: "/appointments/1", method: "PATCH" },
  ],
};


const mockAppointmentListResponse: AppointmentResponse[] = [mockAppointment];

// =========== Tests for Appointments API ==========

describe("Appointments API", () => {
  it("should return appointments list", async () => {
    const query: AppointmentQuery = { userId: "1", pageSize: 10 };

    mockedAxios.get.mockResolvedValueOnce({
      status: 200,
      data: mockAppointmentListResponse,
      headers: { link: 'rel="last" page=1' },
    });

    const result = await getAppointments(query);

    expect(result.content).toHaveLength(1);
    expect(result.content[0].id).toBe(1);
    expect(mockedAxios.get).toHaveBeenCalledWith("/appointments", {
      params: query,
      headers: { Accept: "application/vnd.appointment-list.v1+json" },
    });
  });

  it("should return all confirmed appointments in range", async () => {
    mockedAxios.get.mockResolvedValueOnce({
      status: 200,
      data: mockAppointmentListResponse,
      headers: { link: 'rel="last" page=1' },
    });

    const result = await getAllConfirmedAppointmentsInRange(
      new Date("2024-12-01"),
      new Date("2024-12-31"),
      "1",
    );

    expect(result).toHaveLength(1);
    expect(result[0].id).toBe(1);
    expect(mockedAxios.get).toHaveBeenCalled();
  });

  it("should create a new appointment", async () => {
    const appointmentForm: AppointmentForm = {
      date: parseLocalDate("2024-12-10"),
      timeBlock: "10:00",
      description: "Test appointment",
      doctorId: 1,
    };

    const expectedAppointment = {
      date: "2024-12-10",
      timeBlock: "10:00",
      description: "Test appointment",
      doctorId: 1,
    };

    mockedAxios.post.mockResolvedValueOnce({
      status: 201,
      headers: { location: "/appointments/1" },
    });

    mockedAxios.get.mockResolvedValueOnce({
      status: 200,
      data: mockAppointment,
    });

    const result = await createAppointment(appointmentForm);

    expect(result.id).toBe(1);
    expect(mockedAxios.post).toHaveBeenCalledWith(
      "/appointments",
      expect.objectContaining(expectedAppointment),
      {
        headers: { "Content-Type": "application/vnd.appointment.v1+json" },
      },
    );
    expect(mockedAxios.get).toHaveBeenCalledWith("/appointments/1", {
      headers: { Accept: "application/vnd.appointment.v1+json" },
    });
  });

  it("should throw DoctorNotAvailable if doctor is not available", async () => {
    const appointmentForm: AppointmentForm = {
      date: new Date("2024-12-10"),
      timeBlock: "10:00",
      description: "Test appointment",
      doctorId: 1,
    };

    mockedAxios.post.mockRejectedValueOnce({
      response: {
        status: 409,
        data: { message: "Doctor is not available" },
      },
    });

    await expect(createAppointment(appointmentForm)).rejects.toThrowError(
      DoctorNotAvailable,
    );
  });

  it("should throw PatientNotAvailable if patient is not available", async () => {
    const appointmentForm: AppointmentForm = {
      date: new Date("2024-12-10"),
      timeBlock: "10:00",
      description: "Test appointment",
      doctorId: 1,
    };

    mockedAxios.post.mockRejectedValueOnce({
      response: {
        status: 409,
        data: { message: "Patient is not available" },
      },
    });

    await expect(createAppointment(appointmentForm)).rejects.toThrowError(
      PatientNotAvailable,
    );
  });

  it("should get an appointment by ID", async () => {
    mockedAxios.get.mockResolvedValueOnce({
      status: 200,
      data: mockAppointment,
    });

    const result = await getAppointment("1");

    expect(result.id).toBe(1);
    expect(mockedAxios.get).toHaveBeenCalledWith("/appointments/1", {
      headers: { Accept: "application/vnd.appointment.v1+json" },
    });
  });

  it("should cancel an appointment", async () => {
    const cancelDescription = "Patient canceled the appointment";

    mockedAxios.patch.mockResolvedValueOnce({
      status: 200,
      data: mockAppointment,
    });

    await cancelAppointment("1", cancelDescription);

    expect(mockedAxios.patch).toHaveBeenCalledWith(
      "/appointments/1",
      { status: "CANCELLED", description: cancelDescription },
      {
        headers: {
          "Content-Type": "application/vnd.appointment-cancel.v1+json",
        },
      },
    );
  });
});
