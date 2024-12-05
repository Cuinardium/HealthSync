import React, { useState, useEffect, ReactNode, useCallback } from "react";
import { AxiosError, InternalAxiosRequestConfig } from "axios";

import { axios } from "../api/axios";
import { getTokens, renewAccessToken } from "../api/auth/authApi";

import { AuthContext } from "../context/AuthContext";
import { queryClient } from "../api/queryClient";
import { verifyUser } from "../api/token/tokenApi";
import useLocale from "../hooks/useLocale";
import { useDoctorQueryContext } from "../context/DoctorQueryContext";

const REFRESH_TOKEN_KEY = "healthsync-refresh-token";

const parseJwt = (token: string) => {
  try {
    const base64Payload = token.split(".")[1];
    const decodedPayload = atob(base64Payload);
    return JSON.parse(decodedPayload);
  } catch (e) {
    console.error("Failed to parse JWT", e);
    return null;
  }
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [refreshToken, setRefreshToken] = useState<string | null>(() => {
    if (localStorage.hasOwnProperty(REFRESH_TOKEN_KEY)) {
      return localStorage.getItem(REFRESH_TOKEN_KEY);
    }
    if (sessionStorage.hasOwnProperty(REFRESH_TOKEN_KEY)) {
      return sessionStorage.getItem(REFRESH_TOKEN_KEY);
    }
    return null;
  });

  const [id, setId] = useState<string | null>(null);
  const [role, setRole] = useState<"ROLE_DOCTOR" | "ROLE_PATIENT" | null>(null);
  const [loading, setLoading] = useState<boolean>(
    () =>
      localStorage.hasOwnProperty(REFRESH_TOKEN_KEY) ||
      sessionStorage.hasOwnProperty(REFRESH_TOKEN_KEY),
  );

  const { clearLocale } = useLocale();
  const { resetQuery } = useDoctorQueryContext();

  const authenticated = !!accessToken;

  // Log in and load tokens
  const login = async (
    email: string,
    password: string,
    persistLogin: boolean,
  ): Promise<void> => {
    const credentials = await getTokens(email, password);

    setLoading(true);

    setAccessToken(credentials.accessToken);
    setRefreshToken(credentials.refreshToken);

    if (persistLogin) {
      localStorage.setItem(REFRESH_TOKEN_KEY, credentials.refreshToken);
    }

    sessionStorage.setItem(REFRESH_TOKEN_KEY, credentials.refreshToken);
  };

  const verify = async (email: string, token: string): Promise<void> => {
    const credentials = await verifyUser(email, token);

    setLoading(true);

    setAccessToken(credentials.accessToken);
    setRefreshToken(credentials.refreshToken);

    localStorage.setItem(REFRESH_TOKEN_KEY, credentials.refreshToken);
    sessionStorage.setItem(REFRESH_TOKEN_KEY, credentials.refreshToken);
  };

  // Log out and clear tokens
  const logout = useCallback((): void => {
    setAccessToken(null);
    setRefreshToken(null);
    setId(null);
    setRole(null);

    // Invalidate cache of react query
    queryClient.clear();

    clearLocale();
    resetQuery();

    localStorage.removeItem(REFRESH_TOKEN_KEY);
    sessionStorage.removeItem(REFRESH_TOKEN_KEY);
  }, [clearLocale, resetQuery]);

  // Function to refresh the access token
  const refreshAccessToken = useCallback(async (): Promise<string | null> => {
    if (!refreshToken) return null;

    try {
      const newAccessToken = await renewAccessToken(refreshToken);

      if (!newAccessToken) {
        return null;
      }

      setAccessToken(newAccessToken);

      return newAccessToken;
    } catch (error) {
      // Refresh token is invalid, log out
      logout();
      return null;
    }
  }, [refreshToken, logout]);

  useEffect(() => {
    const initializeAuth = async () => {
      if (refreshToken) {
        await refreshAccessToken();
      }
    };

    initializeAuth();
  }, [refreshAccessToken, refreshToken]);

  useEffect(() => {
    if (accessToken) {
      const parsedToken = parseJwt(accessToken);
      if (parsedToken) {
        setId(parsedToken.id);
        setRole(parsedToken.authorization);
      }

      setLoading(false);
    }
  }, [accessToken]);

  useEffect(() => {
    // Request interceptor to add the access token to the headers
    const requestInterceptor = axios.interceptors.request.use(
      (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
        if (accessToken && config.headers) {
          config.headers["Authorization"] = `Bearer ${accessToken}`;
        }
        return config;
      },
      (error) => Promise.reject(error),
    );

    // Response interceptor to handle token refresh
    const responseInterceptor = axios.interceptors.response.use(
      (response) => response,
      async (error: AxiosError) => {
        const originalRequest = error.config as InternalAxiosRequestConfig & {
          _retry?: boolean;
        };

        if (!originalRequest) {
          return Promise.reject(error);
        }

        // Intercept the 401 and refresh
        if (
          error.response?.status === 401 &&
          refreshToken &&
          !originalRequest._retry
        ) {
          originalRequest._retry = true;

          const newAccessToken = await refreshAccessToken();

          if (newAccessToken && originalRequest.headers) {
            originalRequest.headers["Authorization"] =
              `Bearer ${newAccessToken}`;
            return axios(originalRequest);
          }
        }

        return Promise.reject(error);
      },
    );

    return () => {
      axios.interceptors.request.eject(requestInterceptor);
      axios.interceptors.response.eject(responseInterceptor);
    };
  }, [refreshAccessToken, accessToken, refreshToken]);

  return (
    <AuthContext.Provider
      value={{
        login,
        logout,
        verify,
        accessToken,
        id,
        role,
        authenticated,
        loading,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
