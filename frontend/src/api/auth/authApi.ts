import { axios } from "../axios";
import {AuthenticationError, Credentials, UserNotVerifiedError} from "./auth";

const ACCESS_TOKEN_HEADER = "x-jwt";
const REFRESH_TOKEN_HEADER = "x-refresh";

export async function getTokens(email: string, password: string): Promise<Credentials> {
  const basic = btoa(email + ":" + password);

  try {
    const response = await dummyRequest("Basic " + basic);

    const accessToken = response.headers[ACCESS_TOKEN_HEADER];
    const refreshToken = response.headers[REFRESH_TOKEN_HEADER];

    return { accessToken, refreshToken };
  } catch (error: any) {

    if (error.response?.status === 401) {
      if (error.response.data?.message === "User is disabled") {
        throw new UserNotVerifiedError();
      } else {
        throw new AuthenticationError();
      }
    }

    throw error;
  }
}



export async function renewAccessToken(refreshToken: string): Promise<string> {

  const response = await dummyRequest("Bearer " + refreshToken);

  return response.headers[ACCESS_TOKEN_HEADER];
}


// === Dummy request to get the response headers ====

async function dummyRequest(authorization: string) {
  return axios.get("/doctors", {
    params: {
      pageSize: 1,
    },
    headers: {
      Authorization: authorization,
    },
  });
}
