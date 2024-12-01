import { DoctorQuery } from "../api/doctor/Doctor";
import { createContext, useContext } from "react";

interface DoctorQueryContextType {
  query: DoctorQuery;
  setName: (name: string) => void;
  setCity: (cities: string[]) => void;
  setSpecialty: (specialties: string[]) => void;
  setHealthInsurance: (healthInsurances: string[]) => void;
  setDate: (date: Date | undefined) => void;
  setFromTime: (time: string | undefined) => void;
  setToTime: (time: string | undefined) => void;
  setMinRating: (rating: number | undefined) => void;
  addSpecialty: (specialty: string) => void;
  addHealthInsurance: (healthInsurance: string) => void;
  resetQuery: () => void;
}

export const DoctorQueryContext = createContext<
  DoctorQueryContextType | undefined
>(undefined);

// Hook to access the context
export const useDoctorQueryContext = () => {
  const context = useContext(DoctorQueryContext);
  if (!context) {
    throw new Error(
      "useDoctorQueryContext must be used within a DoctorQueryProvider",
    );
  }
  return context;
};
