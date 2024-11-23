import { useMutation, useQuery } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { Notification } from "../api/notification/Notification";
import { deleteNotification, getNotifications } from "../api/notification/notificationApi";
import { queryClient } from "../api/queryClient";

const STALE_TIME = 1 * 60 * 1000;

// ======= useNotifications ==========

export function useNotifications(userId: string) {
  return useQuery<Notification[], AxiosError>(
    {
      queryKey: ["notifications", userId],
      queryFn: () => getNotifications(userId),
      enabled: !!userId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ====== useDeleteNotification =====

export function useDeleteNotification(
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<void, AxiosError, string>(
    {
      mutationFn: (id: string) => deleteNotification(id),
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ["notifications"] });
        onSuccess();
      },
      onError: (error) => {
        onError(error);
      },
    },
    queryClient,
  );
}


