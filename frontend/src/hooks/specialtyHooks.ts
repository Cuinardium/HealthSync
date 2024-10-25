import { useQuery } from "@tanstack/react-query";
import { getSpecialties, getSpecialty } from "../api/specialty/specialtyApi";
import { Specialty, SpecialtyQuery } from "../api/specialty/Specialty";
import { queryClient } from "../api/queryClient";

const STALE_TIME = 30 * 60 * 1000;

// ========== useSpecialties ==========

export function useSpecialties(specialtyQuery: SpecialtyQuery) {
  return useQuery<Specialty[], Error>(
    {
      queryKey: ["specialties", specialtyQuery],
      queryFn: () => getSpecialties(specialtyQuery),
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useSpecialty ==========

export function useSpecialty(id: string) {
  return useQuery<Specialty, Error>(
    {
      queryKey: ["specialty", id],
      queryFn: () => getSpecialty(id),
      enabled: !!id,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}
