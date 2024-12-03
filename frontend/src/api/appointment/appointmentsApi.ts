import { axios } from "../axios";
import { getPage, Page } from "../page/Page";
import {
  Appointment,
  AppointmentForm,
  AppointmentQuery,
  AppointmentResponse,
  DoctorNotAvailable,
  PatientNotAvailable,
} from "./Appointment";
import { formatDate, parseLocalDate } from "../util/dateUtils";

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
  let fromDateStr;
  if (query.from) {
    fromDateStr = formatDate(query.from);
  }

  let toDateStr;
  if (query.to) {
    toDateStr = formatDate(query.to);
  }

  const queryCopy = {
    ...query,
    from: fromDateStr,
    to: toDateStr,
  };

  const response = await axios.get(APPOINTMENT_ENDPOINT, {
    params: queryCopy,
    headers: { Accept: APPOINTMENT_LIST_CONTENT_TYPE },
  });

  if (response.status === 200) {
    // Set date to Date object
    response.data = response.data?.map((appointment: AppointmentResponse) =>
      mapDetails(appointment),
    );
  }

  return getPage(response);
}

export async function getAllConfirmedAppointmentsInRange(
  from: Date | null,
  to: Date | null,
  userId: string,
): Promise<Appointment[]> {
  let results: Appointment[] = [];
  let nextPageUrl = APPOINTMENT_ENDPOINT;

  let fromDateStr = from ? formatDate(from) : undefined;
  let toDateStr = to ? formatDate(to) : undefined;

  const query = {
    from: fromDateStr,
    to: toDateStr,
    userId,
    status: "CONFIRMED",
    pageSize: 100,
  };

  while (nextPageUrl) {
    const response = await axios.get<AppointmentResponse[]>(nextPageUrl, {
      params: query,
      headers: { Accept: APPOINTMENT_LIST_CONTENT_TYPE },
    });

    if (!response.data || response.status !== 200) {
      break;
    }

    results.push(
      ...response.data.map((appointment) => mapDetails(appointment)),
    );

    const linkHeader = response.headers.link;

    // Get next page URL, parses using RFC 5988 link format
    nextPageUrl = linkHeader?.match(/<([^>]+)>;\s*rel="next"/)?.[1] || null;
  }

  return results;
}

export async function createAppointment(
  appointment: AppointmentForm,
): Promise<Appointment> {
  const body = {
    ...appointment,
    date: formatDate(appointment.date),
  };

  try {
    const response = await axios.post(APPOINTMENT_ENDPOINT, body, {
      headers: { "Content-Type": APPOINTMENT_CONTENT_TYPE },
    });

    const location = response.headers.location;
    const appointmentId = location?.split("/").pop();
    return await getAppointment(appointmentId as string);
  } catch (error: any) {
    if (error.response?.status === 409) {
      console.log(error);
      if (error.response.data?.message.toLowerCase().includes("doctor")) {
        console.log("DoctorNotAvailable");
        throw new DoctorNotAvailable();
      } else {
        console.log("PatientNotAvailable");
        throw new PatientNotAvailable();
      }
    }

    throw error;
  }
}

// =========== appointments/id ==============

export async function getAppointment(id: string): Promise<Appointment> {
  const response = await axios.get<AppointmentResponse>(
    `${APPOINTMENT_ENDPOINT}/${id}`,
    {
      headers: { Accept: APPOINTMENT_CONTENT_TYPE },
    },
  );

  return mapDetails(response.data);
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

export function mapDetails(appointment: AppointmentResponse): Appointment {
  const date = parseLocalDate(appointment.date);

  const doctor = appointment.links.find((link) => link.rel === "doctor")?.href;
  const patient = appointment.links.find(
    (link) => link.rel === "patient",
  )?.href;

  const canIndicate = appointment.links.some(
    (link) => link.rel === "add-indication",
  );

  const canCancel = appointment.links.some(
    (link) => link.rel === "update-self",
  );

  return {
    ...appointment,
    date,
    canIndicate,
    canCancel,
    doctorId: doctor ? (doctor.split("/").pop() as string) : "",
    patientId: patient ? (patient.split("/").pop() as string) : "",
  };
}
