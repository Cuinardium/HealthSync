import { useQuery, useMutation, useInfiniteQuery } from "@tanstack/react-query";
import {
  getVacations,
  getVacation,
  createVacation,
  deleteVacation,
} from "../api/vacation/vacationApi";
import {
  Vacation,
  VacationForm,
} from "../api/vacation/Vacation";
import { queryClient } from "../api/queryClient";
import { AxiosError } from "axios";

const STALE_TIME = 5 * 60 * 1000;

// ========== useVacations ==========

export function useVacations(doctorId: string, pageSize: number) {
  return useInfiniteQuery(
    {
      queryKey: ["vacations", doctorId],
      queryFn: ({ pageParam = 1 }) =>
        getVacations(doctorId, { page: pageParam, pageSize }),
      enabled: !!doctorId,
      getNextPageParam: (lastPage) =>
        lastPage.currentPage < lastPage.totalPages
          ? lastPage.currentPage + 1
          : undefined,
      staleTime: STALE_TIME,
      initialPageParam: 1,
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

export function useCreateVacation(
  doctorId: string,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
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
      },
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
