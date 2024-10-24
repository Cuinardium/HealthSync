import { createContext, useContext } from "react";
import { Doctor } from "../api/doctor/Doctor";
import { Patient } from "../api/patient/Patient";

interface UserContextType {
  user: Patient | Doctor | null;
  isDoctor: boolean | null;
  loading: boolean | null;
}

// Create context with default values
export const UserContext = createContext<UserContextType | null>(null);

// Custom hook to use the AuthContext
export const useUser = (): UserContextType => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser must be used within an UserProvider");
  }
  return context;
};
