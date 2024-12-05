import i18next from "i18next";
import React, { ReactNode, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { UserContext } from "../context/UserContext";
import { useDoctor } from "../hooks/doctorHooks";
import { usePatient } from "../hooks/patientHooks";
import useLocale from "../hooks/useLocale";

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

  const { setLocale } = useLocale();

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

  useEffect(() => {
    if (user?.locale) {
      setLocale(user.locale);
      i18next.changeLanguage(user.locale);
    }
  }, [user, setLocale]);

  return (
    <UserContext.Provider value={{ user, isDoctor, loading }}>
      {children}
    </UserContext.Provider>
  );
};

