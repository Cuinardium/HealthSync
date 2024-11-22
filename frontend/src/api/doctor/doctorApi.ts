import { axios } from "../axios";
import { getHealthInsurance } from "../health-insurance/healthInsuranceApi";
import { getPage, Page } from "../page/Page";
import { getSpecialty } from "../specialty/specialtyApi";
import {
  Doctor,
  DoctorQuery,
  DoctorRegisterForm,
  OccupiedHours,
  AttendingHours,
  DoctorEditForm,
  DoctorResponse,
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

export async function getDoctors(params: DoctorQuery): Promise<Page<Doctor>> {
  const paramsCopy = {
    ...params,
    date: params.date?.toISOString().split("T")[0],
  };

  const response = await axios.get(
    DOCTOR_ENDPOINT,
    {
      params: paramsCopy,
      headers: {
        Accept: DOCTOR_LIST_CONTENT_TYPE,
      },
    },
  );

  if (response.status == 200) {
    // Add health insurances and specialty to each doctor
    response.data = await Promise.all(
      response.data?.map(async (doctor: DoctorResponse) => await mapDoctorDetails(doctor)),
    );
  }

  return getPage(response);
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
  const doctor = doctorResp.data as DoctorResponse;
  return await mapDoctorDetails(doctor);
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

// ========== Utility functions ===========

async function mapDoctorDetails(doctorResp: DoctorResponse): Promise<Doctor> {
  let specialty;
  const specialtyLink = doctorResp.links.find(
    (link) => link.rel === "specialty",
  );

  if (!specialtyLink) {
    throw new Error("Specialty link not found");
  }

  const id = specialtyLink.href.split("/").pop();
  const specialtyResp = await getSpecialty(id as string);

  // To map appropiatelly to translation key
  specialty = specialtyResp.code.toLowerCase().replace(/_/g, ".");

  // Fetch health insurances
  let healthInsurances;
  const healthInsuranceLinks = doctorResp.links.filter(
    (link) => link.rel === "healthinsurance",
  );

  healthInsurances = await Promise.all(
    healthInsuranceLinks.map(async (link) => {
      const id = link.href.split("/").pop();
      const healthInsuranceResp = await getHealthInsurance(id as string);

      // To map appropiatelly to translation key
      return healthInsuranceResp.code.toLowerCase().replace(/_/g, ".");
    }),
  );

  // Can review if the link is present
  const canReview = doctorResp.links.some(
    (link) => link.rel === "create-review",
  );

  const image = doctorResp.links.find((link) => link.rel === "image")?.href;

  return {
    ...doctorResp,
    specialty: specialty,
    healthInsurances: healthInsurances,
    canReview: canReview,
    image: image,
  };
}
