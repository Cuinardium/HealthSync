import React, {
  useState,
  useEffect,
  ReactNode,
} from "react";
import { AxiosError, InternalAxiosRequestConfig } from "axios";

import { axios } from "../api/axios";
import { getTokens, renewAccessToken } from "../api/auth/authApi";

import { AuthContext } from "../context/AuthContext";


const REFRESH_TOKEN_KEY = "healthsync-refresh-token";

interface AuthProviderProps {
  children: ReactNode;
}


export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [refreshToken, setRefreshToken] = useState<string | null>(() =>
    localStorage.getItem(REFRESH_TOKEN_KEY),
  );

  // Log in and load tokens
  const login = async (email: string, password: string): Promise<void> => {
    const credentials = await getTokens(email, password);

    setAccessToken(credentials.accessToken);
    setRefreshToken(credentials.refreshToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, credentials.refreshToken);
  };

  // Log out and clear tokens
  const logout = (): void => {
    setAccessToken(null);
    setRefreshToken(null);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
  };

  // Function to refresh the access token
  const refreshAccessToken = async (): Promise<string | null> => {
    if (!refreshToken) return null;

    try {

      const newAccessToken = await renewAccessToken(refreshToken);

      setAccessToken(newAccessToken);
      return newAccessToken;

    } catch (error) {

      // Refresh token is invalid, log out
      logout(); 
      return null;
    }
  };


  useEffect(() => {

    // Request interceptor to add the access token to the headers
    const requestInterceptor = axios.interceptors.request.use(
      (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
        if (accessToken && config.headers) {
          config.headers['Authorization'] = `Bearer ${accessToken}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Response interceptor to handle token refresh
    const responseInterceptor = axios.interceptors.response.use(
      (response) => response,
      async (error: AxiosError) => {
        const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

        if (!originalRequest) {
          return Promise.reject(error);
        }
        
        // Intercept the 401 and refresh
        if (error.response?.status === 401 && refreshToken && !originalRequest._retry) {
          originalRequest._retry = true;

          const newAccessToken = await refreshAccessToken();

          if (newAccessToken && originalRequest.headers) {
            originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
            return axios(originalRequest);
          }
        }

        return Promise.reject(error);
      }
    );

    return () => {
      axios.interceptors.request.eject(requestInterceptor);
      axios.interceptors.response.eject(responseInterceptor);
    };
  }, [accessToken, refreshToken]);

  return (
    <AuthContext.Provider value={{ login, logout, accessToken }}>
      {children}
    </AuthContext.Provider>
  );
};
