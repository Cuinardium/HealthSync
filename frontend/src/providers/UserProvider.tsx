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
  const [user, setUser] = useState<Doctor | Patient | null>(null);
  const [isDoctor, setIsDoctor] = useState<boolean | null>(null);
  const { authenticated, role, id, loading: authLoading } = useAuth();

  const { data: doctorData, isLoading: isLoadingDoctor } = useDoctor(
    (id && role === "ROLE_DOCTOR" ? id : null) as string,
  );
  const { data: patientData, isLoading: isLoadingPatient } = usePatient(
    (id && role === "ROLE_PATIENT" ? id : null) as string,
  );

  const loading = authLoading || (authenticated && (isLoadingDoctor || isLoadingPatient));

  useEffect(() => {
    if (authenticated) {
      if (role === "ROLE_DOCTOR" && doctorData) {
        setUser(doctorData);
        setIsDoctor(true);
      } else if (role === "ROLE_PATIENT" && patientData) {
        setUser(patientData);
        setIsDoctor(false);
      }
    } else {
      setUser(null)
      setIsDoctor(null)
    }
  }, [authenticated, role, id, isLoadingDoctor, isLoadingPatient]);

  return (
    <UserContext.Provider value={{ user, isDoctor, loading }}>
      {children}
    </UserContext.Provider>
  );
};
