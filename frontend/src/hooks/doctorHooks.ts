import { useQuery, useMutation, useQueryClient, keepPreviousData } from "@tanstack/react-query";
import {
  getDoctors,
  getDoctorById,
} from "../api/doctor/doctorApi";
import {
  DoctorQuery,
  Doctor,
} from "../api/doctor/Doctor";

import { queryClient } from "../api/queryClient";

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

/* // =========== useDoctor =========== */

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
