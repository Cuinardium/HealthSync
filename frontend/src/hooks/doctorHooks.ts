import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  getDoctorById,
} from "../api/doctor/doctorApi";
import {
  Doctor,
} from "../api/doctor/Doctor";

import { queryClient } from "../api/queryClient";

const STALE_TIME = 5 * 60 * 1000;

/* // =========== useDoctorById =========== */

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
