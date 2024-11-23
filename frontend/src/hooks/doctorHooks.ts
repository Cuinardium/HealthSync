import { useQuery, useMutation, useQueryClient, keepPreviousData } from "@tanstack/react-query";
import {
  getDoctors,
  getDoctorById,
  getDoctorAttendingHours,
  updateDoctor,
  updateDoctorAttendingHours,
  createDoctor,
} from "../api/doctor/doctorApi";
import {
  DoctorQuery,
  Doctor,
  AttendingHours,
  DoctorEditForm,
  DoctorRegisterForm,
} from "../api/doctor/Doctor";

import { queryClient } from "../api/queryClient";
import { AxiosError } from "axios";

const STALE_TIME = 5 * 60 * 1000;

// =========== useDoctors ===========

export function useDoctors(query: DoctorQuery) {
  return useQuery(
    {
      queryKey: ["doctors", query],
      queryFn: () => getDoctors(query),
      placeholderData: keepPreviousData,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// =========== useDoctor ===========

export function useDoctor(doctorId: string) {
  return useQuery<Doctor, Error>(
    {
      queryKey: ["doctor", doctorId],
      queryFn: () => getDoctorById(doctorId),
      enabled: !!doctorId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useUpdateDoctor ==========

export function useUpdateDoctor(
  id: string,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<DoctorEditForm, AxiosError, DoctorEditForm>(
    {
      mutationFn: (doctor: DoctorEditForm) => updateDoctor(id, doctor),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["doctor", id],
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

// ========== useCreateDoctor ==========

export function useCreateDoctor(
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<Doctor, AxiosError, DoctorRegisterForm>(
    {
      mutationFn: (doctor: DoctorRegisterForm) => createDoctor(doctor),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["doctors"],
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


// =========== useAttendingHours ===========

export function useAttendingHours(doctorId: string) {
  return useQuery<AttendingHours[], Error>(
    {
      queryKey: ["attendingHours", doctorId],
      queryFn: () => getDoctorAttendingHours(doctorId),
      enabled: !!doctorId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}


// =========== useUpdateAttendingHours ===========

export function useUpdateAttendingHours(
  doctorId: string,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<AttendingHours[], AxiosError, AttendingHours[]>(
    {
      mutationFn: (attendingHours: AttendingHours[]) =>
        updateDoctorAttendingHours(doctorId, attendingHours),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["attendingHours", doctorId],
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
