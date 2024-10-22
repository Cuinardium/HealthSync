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

export function useCreateAppointment() {
  return useMutation<Appointment, Error, AppointmentForm>(
    {
      mutationFn: (appointment: AppointmentForm) =>
        createAppointment(appointment),
      onSuccess: (_) => {
        queryClient.invalidateQueries({
          queryKey: ["appointments"],
        });
      },
    },
    queryClient,
  );
}

// ========== useCancelAppointment ==========

export function useCancelAppointment() {
  return useMutation<Appointment, Error, { id: string; description: string }>(
    {
      mutationFn: ({ id, description }) => cancelAppointment(id, description),
      onSuccess: (cancelledAppointment) => {
        queryClient.invalidateQueries({
          queryKey: ["appointment", cancelledAppointment.id],
        });
      },
    },
    queryClient,
  );
}
