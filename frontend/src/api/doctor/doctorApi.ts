import { axios } from "../axios";
import { getPage, Page } from "../page/Page";
import {
  AttendingHours,
  Doctor,
  DoctorEditForm,
  DoctorQuery,
  DoctorRegisterForm,
  DoctorResponse,
  OccupiedHours,
} from "./Doctor";

const DOCTOR_ENDPOINT = "/doctors";
const ATTENDING_HOURS_ENDPOINT = "/attendinghours";
const OCCUPIED_HOURS_ENDPOINT = "/occupiedhours";

const DOCTOR_CONTENT_TYPE = "application/vnd.doctor.v1+json";
const DOCTOR_LIST_CONTENT_TYPE = "application/vnd.doctor-list.v1+json";
const ATTENDING_HOURS_CONTENT_TYPE =
  "application/vnd.attending-hours-list.v1+json";
const OCCUPIED_HOURS_CONTENT_TYPE =
  "application/vnd.occupied-hours-list.v1+json";

// ========== doctors ==============

export async function getDoctors(
  params: DoctorQuery,
): Promise<Page<DoctorResponse>> {
  const paramsCopy = {
    ...params,
    date: params.date?.toISOString().split("T")[0],
  };

  const response = await axios.get(DOCTOR_ENDPOINT, {
    params: paramsCopy,
    headers: {
      Accept: DOCTOR_LIST_CONTENT_TYPE,
    },
  });
  return getPage(response);
}

export async function createDoctor(doctor: DoctorRegisterForm): Promise<void> {
  await axios.post<Doctor>(DOCTOR_ENDPOINT, doctor, {
    headers: {
      "Content-Type": DOCTOR_CONTENT_TYPE,
    },
  });
}

// ========== doctors/id ==========

export async function getDoctorById(id: String): Promise<DoctorResponse> {
  const doctorResp = await axios.get(DOCTOR_ENDPOINT + "/" + id, {
    headers: {
      Accept: DOCTOR_CONTENT_TYPE,
    },
  });

  return doctorResp.data as DoctorResponse;
}

export async function updateDoctor(
  id: string,
  doctor: DoctorEditForm,
): Promise<DoctorEditForm> {
  // Multipart/form-data
  const formData = new FormData();

  // Doctor data
  formData.append("name", doctor.name);
  formData.append("lastname", doctor.lastname);
  formData.append("locale", doctor.locale);

  doctor.healthInsurances.forEach((healthInsurance) =>
    formData.append("healthInsurance", healthInsurance),
  );

  formData.append("city", doctor.city);
  formData.append("address", doctor.address);
  formData.append("specialty", doctor.specialty);

  // Profile picture
  if (doctor.image) {
    formData.append("image", doctor.image);
  }

  await axios.put(DOCTOR_ENDPOINT + "/" + id, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return doctor;
}

// ========== doctors/id/attendinghours =================

export async function getDoctorAttendingHours(
  doctorId: string,
): Promise<AttendingHours[]> {
  const attendingHoursResp = await axios.get(
    DOCTOR_ENDPOINT + "/" + doctorId + ATTENDING_HOURS_ENDPOINT,
    {
      headers: {
        Accept: ATTENDING_HOURS_CONTENT_TYPE,
      },
    },
  );

  return attendingHoursResp.data as AttendingHours[];
}

export async function updateDoctorAttendingHours(
  doctorId: string,
  attendingHours: AttendingHours[],
): Promise<AttendingHours[]> {
  await axios.put(
    DOCTOR_ENDPOINT + "/" + doctorId + ATTENDING_HOURS_ENDPOINT,
    attendingHours,
    {
      headers: {
        "Content-Type": ATTENDING_HOURS_CONTENT_TYPE,
      },
    },
  );

  return attendingHours;
}

// ========= doctors/id/occupiedhours ==========

export async function getDoctorOccupiedHours(
  doctorId: string,
  from: Date | null,
  to: Date | null,
): Promise<OccupiedHours[]> {
  let results: OccupiedHours[] = [];
  let nextPageUrl = DOCTOR_ENDPOINT + "/" + doctorId + OCCUPIED_HOURS_ENDPOINT;

  while (nextPageUrl) {
    const occupiedHoursResp = await axios.get<OccupiedHours[]>(nextPageUrl, {
      headers: {
        Accept: OCCUPIED_HOURS_CONTENT_TYPE,
      },
      params: {
        from: from?.toISOString().split("T")[0],
        to: to?.toISOString().split("T")[0],
        pageSize: 365,
      },
    });
    results.push(...occupiedHoursResp.data);

    const linkHeader = occupiedHoursResp.headers.link;

    // Get next page URL, parses using RFC 5988 link format
    nextPageUrl = linkHeader?.match(/<([^>]+)>;\s*rel="next"/)?.[1] || null;
  }

  return results;
}