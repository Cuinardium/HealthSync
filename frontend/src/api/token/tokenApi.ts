import { Credentials } from "../auth/auth";
import { axios } from "../axios";

const VERIFICATION_TOKEN_ENDPOINT = "/tokens/verification";
const EMAIL_CONTENT_TYPE = "application/vnd.email.v1+json";

const ACCESS_TOKEN_HEADER = "x-jwt";
const REFRESH_TOKEN_HEADER = "x-refresh";

// ================ verification ================

export async function resendVerificationToken(email: string): Promise<void> {
  const emailBody = {
    email: email,
  };

  await axios.post<void>(VERIFICATION_TOKEN_ENDPOINT, emailBody, {
    headers: {
      "Content-Type": EMAIL_CONTENT_TYPE,
    },
  });
}

export async function verifyUser(
  email: string,
  token: string,
): Promise<Credentials> {
  const emailBody = {
    email: email,
  };

  const response = await axios.patch<void>(
    `${VERIFICATION_TOKEN_ENDPOINT}/${token}`,
    emailBody,
    {
      headers: {
        "Content-Type": EMAIL_CONTENT_TYPE,
      },
    },
  );

  const accessToken = response.headers[ACCESS_TOKEN_HEADER];
  const refreshToken = response.headers[REFRESH_TOKEN_HEADER];

  return { accessToken, refreshToken };
}
