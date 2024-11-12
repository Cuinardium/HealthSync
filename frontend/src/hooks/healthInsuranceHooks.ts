import { useQuery } from "@tanstack/react-query";
import { getHealthInsurances, getHealthInsurance } from "../api/health-insurance/healthInsuranceApi";
import { HealthInsurance } from "../api/health-insurance/HealthInsurance";
import { queryClient } from "../api/queryClient";

const STALE_TIME = 30 * 60 * 1000;

// ========== useHealthInsurances ==========

export function useHealthInsurances() {
  return useQuery<HealthInsurance[], Error>(
    {
      queryKey: ["healthInsurances"],
      queryFn: () => getHealthInsurances(),
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useHealthInsurance ==========

export function useHealthInsurance(id: string) {
  return useQuery<HealthInsurance, Error>(
    {
      queryKey: ["healthInsurance", id],
      queryFn: () => getHealthInsurance(id),
      enabled: !!id,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}
