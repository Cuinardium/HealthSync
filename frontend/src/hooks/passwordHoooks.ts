import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { PasswordForm } from "../api/password/Password";
import { updatePassword } from "../api/password/passwordApi";
import { queryClient } from "../api/queryClient";

// ======== useUpdatePassword ========

export function useUpdatePassword(
  userId: string,
  isDoctor: boolean,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<void, AxiosError, PasswordForm>(
    {
      mutationFn: (password: PasswordForm) =>
        updatePassword(userId, isDoctor, password),
      onSuccess: () => {
        onSuccess();
      },
      onError: (error) => {
        onError(error);
      },
    },
    queryClient,
  );
}
