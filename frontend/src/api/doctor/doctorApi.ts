import { axios } from "../axios";
import { AttendingHours } from "../models/AttendingHours";
import { HealthInsurance } from "../models/HealthInsurance";
import { Specialty } from "../specialty/Specialty";
import { getSpecialty } from "../specialty/specialtyApi";
import {
  Doctor,
  DoctorQuery,
  DoctorRegisterForm,
  OccupiedHours,
} from "./Doctor";

const DOCTOR_ENDPOINT = "/doctors";
const ATTENDING_HOURS_ENDPOINT = "/attendinghours";
const OCCUPIED_HOURS_ENDPOINT = "/occupiedhours";

const DOCTOR_CONTENT_TYPE = "application/vnd.doctor.v1+json";
const DOCTOR_LIST_CONTENT_TYPE = "application/vnd.doctor-list.v1+json";
const SPECIALTY_CONTENT_TYPE = "application/vnd.specialty.v1+json";
const HEALTH_INSURANCE_CONTENT_TYPE =
  "application/vnd.health-insurance.v1+json";
const ATTENDING_HOURS_CONTENT_TYPE =
  "application/vnd.attending-hours-list.v1+json";
const OCCUPIED_HOURS_CONTENT_TYPE =
  "application/vnd.occupied-hours-list.v1+json";

// ========== doctors ==============

export async function getDoctors(params: DoctorQuery): Promise<Doctor[]> {
  const response = await axios.get<Doctor[]>(
    DOCTOR_ENDPOINT,
    {
      params: params,
      headers: {
        Accept: DOCTOR_LIST_CONTENT_TYPE,
      },
    },
  );

  // No content
  if (response.status === 204) {
    return [];
  }

  // Add health insurances and specialty to each doctor
  const doctors = await Promise.all(
    response.data.map(async (doctor) => await mapDoctorDetails(doctor)),
  );

  return doctors;
}

export async function createDoctor(
  doctor: DoctorRegisterForm,
): Promise<Doctor> {
  const response = await axios.post<Doctor>(DOCTOR_ENDPOINT, doctor, {
    headers: {
      "Content-Type": DOCTOR_CONTENT_TYPE,
    },
  });
    
  const location = response.headers.location;
  const doctorId = location?.split("/").pop();
  return await getDoctorById(doctorId as string);
}

// ========== doctors/id ==========

export async function getDoctorById(id: String): Promise<Doctor> {
  const doctorResp = await axios.get(DOCTOR_ENDPOINT + "/" + id, {
    headers: {
      Accept: DOCTOR_CONTENT_TYPE,
    },
  });

  // Add health insurances and specialty to doctor
  const doctor = doctorResp.data as Doctor;
  return await mapDoctorDetails(doctor);
}

export async function updateDoctor(
  doctor: Doctor,
  profilePicture: File | null,
  locale: string,
): Promise<Doctor> {
  // Multipart/form-data
  const formData = new FormData();

  // Doctor data
  formData.append("name", doctor.firstName);
  formData.append("lastname", doctor.lastName);
  formData.append("email", doctor.email);
  formData.append("locale", locale);

  doctor.healthInsurances.forEach((healthInsurance) =>
    formData.append("healthInsurance", healthInsurance),
  );

  formData.append("city", doctor.city);
  formData.append("specialty", doctor.specialty);

  // Profile picture
  if (profilePicture) {
    formData.append("image", profilePicture);
  }

  await axios.put(DOCTOR_ENDPOINT + "/" + doctor.id, formData, {
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

// ========== Utility functions ===========

async function fetchHealthInsurances(
  insuranceUrls: string[],
): Promise<string[]> {
  return Promise.all(
    insuranceUrls.map(async (insuranceUrl) => {
      const insuranceResp = await axios.get(insuranceUrl, {
        headers: {
          Accept: HEALTH_INSURANCE_CONTENT_TYPE,
        },
      });
      const healthInsurance = insuranceResp.data as HealthInsurance;
      return healthInsurance.code;
    }),
  );
}

async function mapDoctorDetails(doctor: Doctor): Promise<Doctor> {
  // Fetch specialty details
  if (doctor.specialty) {
    const id = doctor.specialty.split("/").pop();
    const specialty = await getSpecialty(id as string);
    doctor.specialty = specialty.code
  }

  // Fetch health insurances
  if (doctor.healthInsurances && doctor.healthInsurances.length > 0) {
    doctor.healthInsurances = await fetchHealthInsurances(
      doctor.healthInsurances,
    );
  }

  return doctor;
}
