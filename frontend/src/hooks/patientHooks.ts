import { useMutation, useQuery } from "@tanstack/react-query";
import {
  createPatient,
  getPatient,
  updatePatient,
} from "../api/patient/patientApi";
import {
  Patient,
  PatientEditForm,
  PatientRegisterForm,
  PatientResponse,
} from "../api//patient/Patient";
import { queryClient } from "../api/queryClient";
import { AxiosError } from "axios";
import { getHealthInsurance } from "../api/health-insurance/healthInsuranceApi";
import { HealthInsurance } from "../api/health-insurance/HealthInsurance";

const STALE_TIME = 5 * 60 * 1000;

// ========== usePatient ==========

export function usePatient(id: string) {
  return useQuery<Patient, Error>(
    {
      queryKey: ["patient", id],
      queryFn: async () => {
        const patientResponse = await getPatient(id);
        return mapPatientDetails(patientResponse);
      },
      enabled: !!id,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useCreatePatient ==========

export function useCreatePatient(
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<void, AxiosError, PatientRegisterForm>(
    {
      mutationFn: (patient: PatientRegisterForm) => createPatient(patient),
      onSuccess: () => {
        onSuccess();
      },
      onError: (error) => {
        onError(error);
      },
    },
    queryClient,
  );
}

// ========== useUpdatePatient ==========

export function useUpdatePatient(
  id: string,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<PatientEditForm, AxiosError, PatientEditForm>(
    {
      mutationFn: (patient: PatientEditForm) => updatePatient(id, patient),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["patient", id],
        });
        onSuccess();
      },
      onError: (error) => {
        onError(error);
      },
    },
    queryClient,
  );
}

// ========== utility functions ==========

async function mapPatientDetails(
  patientResponse: PatientResponse,
): Promise<Patient> {
  const healthInsuranceId = patientResponse.links
    .find((link) => link.rel === "healthinsurance")
    ?.href.split("/")
    .pop();
  const healthInsuranceCacheKey = ["healthInsurance", healthInsuranceId];

  let healthInsuranceResp: HealthInsurance;

  if (queryClient.getQueryData(healthInsuranceCacheKey)) {
    healthInsuranceResp = queryClient.getQueryData(
      healthInsuranceCacheKey,
    ) as HealthInsurance;
  } else {
    healthInsuranceResp = await getHealthInsurance(healthInsuranceId as string);
    queryClient.setQueryData(healthInsuranceCacheKey, healthInsuranceResp);
  }

  // To map appropiatelly to translation key
  const healthInsurance = healthInsuranceResp.code
    .toLowerCase()
    .replace(/_/g, ".");

  const image = patientResponse.links.find(
    (link) => link.rel === "image",
  )?.href;

  return {
    ...patientResponse,
    healthInsurance,
    image,
  };
}
