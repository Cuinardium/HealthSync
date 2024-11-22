import { axios } from "../axios";
import { PasswordForm } from "./Password";

const USER_ENDPOINT = (userId: string, isDoctor: boolean) => {
  return isDoctor ? `/doctors/${userId}` : `/patients/${userId}`;
};

const PASSWORD_CONTENT_TYPE = "application/vnd.password.v1+json";

// ========== password ==============

export async function updatePassword(
  userId: string,
  isDoctor: boolean,
  password: PasswordForm,
): Promise<void> {
  await axios.patch(USER_ENDPOINT(userId, isDoctor), password, {
    headers: {
      "Content-Type": PASSWORD_CONTENT_TYPE,
    },
  });
}
