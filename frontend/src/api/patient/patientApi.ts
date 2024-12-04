import { axios } from "../axios";
import { Patient, PatientRegisterForm, PatientEditForm, PatientResponse } from "./Patient";

const PATIENT_ENDPOINT = "/patients";

const PATIENT_CONTENT_TYPE = "application/vnd.patient.v1+json";

// ========== patients ==============

export async function createPatient(patient: PatientRegisterForm): Promise<void> {
  await axios.post<Patient>(PATIENT_ENDPOINT, patient, {
    headers: {
      "Content-Type": PATIENT_CONTENT_TYPE,
    },
  });
}

// ========== patients/id ==========

export async function getPatient(id: string): Promise<PatientResponse> {
  const response = await axios.get<PatientResponse>(`${PATIENT_ENDPOINT}/${id}`, {
    headers: {
      Accept: PATIENT_CONTENT_TYPE,
    },
  });

  return response.data;
}

export async function updatePatient(
  id: string,
  patient: PatientEditForm,
): Promise<PatientEditForm> {
  const formData = new FormData();

  // Patient data
  formData.append("name", patient.name);
  formData.append("lastname", patient.lastname);
  formData.append("locale", patient.locale);

  formData.append("healthInsurance", patient.healthInsurance);

  // Profile picture
  if (patient.image) {
    formData.append("image", patient.image);
  }

  await axios.put(`${PATIENT_ENDPOINT}/${id}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return patient;
}