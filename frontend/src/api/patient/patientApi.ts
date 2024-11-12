import { axios } from "../axios";
import { getHealthInsurance } from "../health-insurance/healthInsuranceApi";
import { Patient, PatientRegisterForm, PatientEditForm } from "./Patient";

const PATIENT_ENDPOINT = "/patients";

const PATIENT_CONTENT_TYPE = "application/vnd.patient.v1+json";

// ========== patients ==============

export async function createPatient(patient: PatientRegisterForm): Promise<Patient> {
  const response = await axios.post<Patient>(PATIENT_ENDPOINT, patient, {
    headers: {
      "Content-Type": PATIENT_CONTENT_TYPE,
    },
  });

  const location = response.headers.location;
  const patientId = location?.split("/").pop();

  return await getPatient(patientId as string);
}

// ========== patients/id ==========

export async function getPatient(id: string): Promise<Patient> {
  const response = await axios.get<Patient>(`${PATIENT_ENDPOINT}/${id}`, {
    headers: {
      Accept: PATIENT_CONTENT_TYPE,
    },
  });

  return await mapPatientDetails(response.data);
}

export async function updatePatient(
  id: string,
  patient: PatientEditForm,
): Promise<PatientEditForm> {
  const formData = new FormData();

  // Patient data
  formData.append("name", patient.name);
  formData.append("lastname", patient.lastname);
  formData.append("email", patient.email);
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

// ========== auxiliary functions ==========

async function mapPatientDetails(patient: Patient): Promise<Patient> {
  const healthInsuranceId = patient.healthInsurance.split("/").pop();
  const healthInsurance = await getHealthInsurance(healthInsuranceId as string);

  // To map appropiatelly to translation key
  patient.healthInsurance = healthInsurance.code.toLowerCase().replace(/_/g,".");
  return patient;
}
