import {
  useQuery,
  useMutation,
  useInfiniteQuery,
  useQueries,
} from "@tanstack/react-query";
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
import { useNotifications } from "./notificationHooks";

const STALE_TIME = 5 * 60 * 1000;

// ========== useAppointments ==========

export function useAppointments(query: AppointmentQuery) {
  return useInfiniteQuery(
    {
      queryKey: ["appointments", query],
      queryFn: ({ pageParam = 1 }) =>
        getAppointments({ ...query, page: pageParam }),
      staleTime: STALE_TIME,
      enabled: !!query.userId,
      getNextPageParam: (lastPage: Page<Appointment>) =>
        lastPage.currentPage < lastPage.totalPages
          ? lastPage.currentPage + 1
          : undefined,
      initialPageParam: 1,
    },
    queryClient,
  );
}

// ========== useAppointment ==========

export function useAppointment(id: string) {
  return useQuery<Appointment, AxiosError>(
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
  return useMutation<Appointment, AxiosError, string>(
    {
      mutationFn: (description) =>
        cancelAppointment(appointmentId, description),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["appointment", appointmentId],
        });

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

// ========== use appointments with notifications ==========

export function useAppointmentsFromNotifications(userId: string) {
  const { data: notifications, isLoading: isLoadingNotifications } =
    useNotifications(userId);

  const appointmentsQueries = useQueries({
    queries:
      notifications?.map((notification) => ({
        queryKey: ["appointment", notification.appointmentId],
        queryFn: () => getAppointment(String(notification.appointmentId)),
        enabled: !!notification.appointmentId,
        staleTime: STALE_TIME,
      })) || [],
  });

  if (isLoadingNotifications) {
    return { appointments: [], isLoading: true };
  }

  const appointments: Appointment[] = appointmentsQueries
    .map((query) => query.data)
    .filter(
      (appointment): appointment is Appointment =>
        appointment !== undefined && appointment !== null,
    );

  const errors = appointmentsQueries.filter((query) => query.isError);
  const error = errors.length > 0 ? errors[0].error : null;

  return {
    appointments,
    isLoading:
      isLoadingNotifications ||
      appointmentsQueries.some((query) => query.isLoading),
    error,
  };
}
