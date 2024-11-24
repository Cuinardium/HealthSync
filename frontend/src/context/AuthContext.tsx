import { createContext, useContext } from "react";

// Define types for AuthContext
interface AuthContextType {
  login: (email: string, password: string, persistLogin: boolean) => Promise<void>;
  logout: () => void;
  verify: (email: string, token: string) => Promise<void>;
  accessToken: string | null;
  id: string | null;
  role: "ROLE_DOCTOR" | "ROLE_PATIENT" | null;
  authenticated: boolean | null;
  loading: boolean | null;
}

// Create context with default values
export const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Custom hook to use the AuthContext
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
