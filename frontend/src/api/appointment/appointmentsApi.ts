import { axios } from "../axios";
import { getPage, Page } from "../page/Page";
import { Appointment, AppointmentForm, AppointmentQuery } from "./Appointment";

const APPOINTMENT_ENDPOINT = "/appointments";

const APPOINTMENT_CONTENT_TYPE = "application/vnd.appointment.v1+json";
const APPOINTMENT_LIST_CONTENT_TYPE =
  "application/vnd.appointment-list.v1+json";
const APPOINTMENT_CANCEL_CONTENT_TYPE =
  "application/vnd.appointment-cancel.v1+json";

// =========== appointments ==============

export async function getAppointments(
  query: AppointmentQuery,
): Promise<Page<Appointment>> {
  const response = await axios.get<Appointment[]>(APPOINTMENT_ENDPOINT, {
    params: query,
    headers: { Accept: APPOINTMENT_LIST_CONTENT_TYPE },
  });

  response.data = response.data?.map((appointment) => mapDates(appointment));

  return getPage(response);
}

export async function createAppointment(
  appointment: AppointmentForm,
): Promise<Appointment> {
  (appointment as any).date = appointment.date.toISOString().split("T")[0];

  const response = await axios.post(APPOINTMENT_ENDPOINT, appointment, {
    headers: { "Content-Type": APPOINTMENT_CONTENT_TYPE },
  });

  const location = response.headers.location;
  const appointmentId = location?.split("/").pop();

  return await getAppointment(appointmentId as string);
}

// =========== appointments/id ==============

export async function getAppointment(id: string): Promise<Appointment> {
  const response = await axios.get<Appointment>(
    `${APPOINTMENT_ENDPOINT}/${id}`,
    {
      headers: { Accept: APPOINTMENT_CONTENT_TYPE },
    },
  );

  return mapDates(response.data);
}

export async function cancelAppointment(
  id: string,
  description: string,
): Promise<Appointment> {
  const body = {
    status: "CANCELLED",
    description,
  };

  const response = await axios.patch<Appointment>(
    `${APPOINTMENT_ENDPOINT}/${id}`,
    body,
    {
      headers: { "Content-Type": APPOINTMENT_CANCEL_CONTENT_TYPE },
    },
  );

  return response.data;
}

// ========= auxiliary functions =========

export function mapDates(appointment: Appointment): Appointment {
  appointment.date = new Date(appointment.date);
  return appointment;
}
