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
} from "../api//patient/Patient";
import { queryClient } from "../api/queryClient";
import { AxiosError } from "axios";

const STALE_TIME = 5 * 60 * 1000;

// ========== usePatient ==========

export function usePatient(id: string) {
  return useQuery<Patient, Error>(
    {
      queryKey: ["patient", id],
      queryFn: () => getPatient(id),
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
  return useMutation<Patient, AxiosError, PatientRegisterForm>(
    {
      mutationFn: (patient: PatientRegisterForm) => createPatient(patient),
      onSuccess: (newPatient) => {
        queryClient.invalidateQueries({ queryKey: ["patient", newPatient.id] });
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
