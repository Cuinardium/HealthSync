import { useMutation, useQuery } from "@tanstack/react-query";
import { createPatient, getPatient, updatePatient } from "../api/patient/patientApi";
import {
  Patient,
  PatientEditForm,
  PatientRegisterForm,
} from "../api//patient/Patient";
import { queryClient } from "../api/queryClient";

export function usePatient(id: string) {
  return useQuery<Patient, Error>(
    {
      queryKey: ["patient", id],
      queryFn: () => getPatient(id),
      enabled: !!id,
      staleTime: 5 * 60 * 1000,
    },
    queryClient,
  );
}

export function useCreatePatient() {
  return useMutation<Patient, Error, PatientRegisterForm>(
    {
      mutationFn: (patient: PatientRegisterForm) => createPatient(patient),
      onSuccess: (newPatient) => {
        queryClient.invalidateQueries({ queryKey: ["patient", newPatient.id] });
      },
    },
    queryClient,
  );
}

export function useUpdatePatient(id: string) {
  return useMutation<PatientEditForm, Error, PatientEditForm>(
    {
      mutationFn: (patient: PatientEditForm) => updatePatient(id, patient),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["patient", id],
        });
      },
    },
    queryClient,
  );
}
