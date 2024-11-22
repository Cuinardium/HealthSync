import { ReactNode, useEffect, useState } from "react";
import { Doctor } from "../api/doctor/Doctor";
import { Patient } from "../api/patient/Patient";
import { useAuth } from "../context/AuthContext";
import { UserContext } from "../context/UserContext";
import { useDoctor } from "../hooks/doctorHooks";
import { usePatient } from "../hooks/patientHooks";

interface UserProviderProps {
  children: ReactNode;
}


export const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
  const { authenticated, role, id, loading: authLoading } = useAuth();

  const { data: doctorData, isLoading: isLoadingDoctor } = useDoctor(
    (role === "ROLE_DOCTOR" ? id : null) as string
  );
  const { data: patientData, isLoading: isLoadingPatient } = usePatient(
    (role === "ROLE_PATIENT" ? id : null) as string
  );

  const data =
    authenticated && role === "ROLE_DOCTOR"
      ? doctorData
      : authenticated && role === "ROLE_PATIENT"
      ? patientData
      : null;

  const user = data ? data : null

  const isDoctor =
    authenticated && role === "ROLE_DOCTOR"
      ? true
      : authenticated && role === "ROLE_PATIENT"
      ? false
      : null;

  const loading = authLoading || isLoadingDoctor || isLoadingPatient;

  return (
    <UserContext.Provider value={{ user, isDoctor, loading }}>
      {children}
    </UserContext.Provider>
  );
};

