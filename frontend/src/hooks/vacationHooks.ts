import { useQuery, useMutation } from "@tanstack/react-query";
import {
  getVacations,
  getVacation,
  createVacation,
  deleteVacation,
} from "../api/vacation/vacationApi";
import {
  Vacation,
  VacationForm,
  VacationQuery,
} from "../api/vacation/Vacation";
import { queryClient } from "../api/queryClient";
import { Page } from "../api/page/Page";
import { AxiosError } from "axios";

const STALE_TIME = 5 * 60 * 1000;

// ========== useVacations ==========

export function useVacations(doctorId: string, query: VacationQuery) {
  return useQuery<Page<Vacation>, AxiosError>(
    {
      queryKey: ["vacations", doctorId, query.page],
      queryFn: () => getVacations(doctorId, query),
      enabled: !!doctorId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useVacation ==========

export function useVacation(doctorId: string, vacationId: string) {
  return useQuery<Vacation, AxiosError>(
    {
      queryKey: ["vacation", doctorId, vacationId],
      queryFn: () => getVacation(doctorId, vacationId),
      enabled: !!doctorId && !!vacationId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useCreateVacation ==========

export function useCreateVacation(doctorId: string, onSuccess: () => void, onError: (error: AxiosError) => void) {
  return useMutation<Vacation, AxiosError, VacationForm>(
    {
      mutationFn: (vacation: VacationForm) =>
        createVacation(doctorId, vacation),
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["vacations", doctorId] });
        onSuccess();
      },
      onError: (error) => {
        onError(error);
      }
    },
    queryClient,
  );
}

// ========== useDeleteVacation ==========

export function useDeleteVacation(doctorId: string) {
  return useMutation<void, AxiosError, string>(
    {
      mutationFn: (vacationId: string) => deleteVacation(doctorId, vacationId),
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["vacations", doctorId] });
      },
    },
    queryClient,
  );
}
