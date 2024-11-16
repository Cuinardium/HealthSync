import { useQuery, useMutation, keepPreviousData } from "@tanstack/react-query";
import { queryClient } from "../api/queryClient"; // Custom QueryClient instance
import {
  getAppointments,
  createAppointment,
  getAppointment,
  cancelAppointment,
} from "../api/appointment/appointmentsApi";
import {
  Appointment,
  AppointmentForm,
  AppointmentQuery,
} from "../api/appointment/Appointment";
import { Page } from "../api/page/Page";
import { AxiosError } from "axios";

const STALE_TIME = 5 * 60 * 1000;

// ========== useAppointments ==========

export function useAppointments(query: AppointmentQuery) {
  return useQuery<Page<Appointment>, Error>(
    {
      queryKey: ["appointments", query],
      queryFn: () => getAppointments(query),
      staleTime: STALE_TIME,
      enabled: !!query.userId,
      placeholderData: keepPreviousData,
    },
    queryClient,
  );
}

// ========== useAppointment ==========

export function useAppointment(id: string) {
  return useQuery<Appointment, Error>(
    {
      queryKey: ["appointment", id],
      queryFn: () => getAppointment(id),
      staleTime: STALE_TIME,
      enabled: !!id,
    },
    queryClient,
  );
}

// ========== useCreateAppointment ==========

export function useCreateAppointment(
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<Appointment, AxiosError, AppointmentForm>(
    {
      mutationFn: (appointment: AppointmentForm) =>
        createAppointment(appointment),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["appointments"],
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

// ========== useCancelAppointment ==========

export function useCancelAppointment(
  appointmentId: string,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<
    Appointment,
    AxiosError,
    string
  >(
    {
      mutationFn: (description) => cancelAppointment(appointmentId, description),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["appointment", appointmentId],
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
