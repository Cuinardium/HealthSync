import React, { ReactNode, useState } from "react";
import { DoctorQuery } from "../api/doctor/Doctor";
import { DoctorQueryContext } from "../context/DoctorQueryContext";
import { Time, TIMES, TIMES_WITH_BLOCK_END } from "../api/time/Time";
import { isSameDay } from "../api/util/dateUtils";

interface DoctorQueryProviderProps {
  children: ReactNode;
}

const defaultQuery: DoctorQuery = {
  pageSize: 10,
};

export const DoctorQueryProvider: React.FC<DoctorQueryProviderProps> = ({
  children,
}) => {
  const [query, setQuery] = useState<DoctorQuery>(defaultQuery);

  // Helper functions to update specific fields
  const setName: (name: string) => void = (name: string | undefined) =>
    setQuery((prev) => ({ ...prev, name }));
  const setCity = (cities: string[]) =>
    setQuery((prev) => ({ ...prev, city: cities }));
  const setSpecialty = (specialties: string[]) =>
    setQuery((prev) => ({ ...prev, specialty: specialties }));
  const setHealthInsurance = (healthInsurances: string[]) =>
    setQuery((prev) => ({ ...prev, healthInsurance: healthInsurances }));
  const setDate = (date: Date | undefined) =>
    setQuery((prev) => {
      let newDate = prev.date;

      if (!isSameDay(date, prev.date)) {
        newDate = date;
      }

      let fromTime = prev.fromTime as Time;
      let toTime = prev.toTime as Time;
      if (date && (!prev.fromTime || !prev.toTime)) {
        fromTime = "08:00";
        toTime = "18:00";
      }

      return { ...prev, date: newDate, fromTime, toTime };
    });
  const setFromTime = (time: string | undefined) =>
    setQuery((prev) => {
      const fromTime = time as Time;
      let toTime = prev.toTime as Time;

      if (fromTime) {
        const fromIndex = TIMES.indexOf(fromTime as Time);
        let toIndex = (TIMES.indexOf(toTime as Time) - 1) % TIMES.length;
        if (toIndex < 0) {
          toIndex = TIMES.length - 1;
        }

        if (toIndex < fromIndex) {
          toTime = TIMES_WITH_BLOCK_END[fromIndex];
        }
      }

      return { ...prev, fromTime: time, toTime };
    });
  const setToTime = (time: string | undefined) =>
    setQuery((prev) => ({ ...prev, toTime: time }));
  const setMinRating = (rating: number | undefined) =>
    setQuery((prev) => ({
      ...prev,
      minRating: rating === 0 ? undefined : rating,
    }));
  const addSpecialty = (specialty: string) => {
    const specialties = query.specialty || [];

    if (!query.specialty?.includes(specialty)) {
      specialties.push(specialty);
    }

    setQuery((prev) => ({
      ...prev,
      specialty: specialties,
    }));
  };
  const addHealthInsurance = (healthInsurance: string) => {
    const healthInsurances = query.healthInsurance || [];

    if (!query.healthInsurance?.includes(healthInsurance)) {
      healthInsurances.push(healthInsurance);
    }

    setQuery((prev) => ({
      ...prev,
      healthInsurance: healthInsurances,
    }));
  };
  const resetQuery = () => setQuery(defaultQuery);

  return (
    <DoctorQueryContext.Provider
      value={{
        query,
        setName,
        setCity,
        setSpecialty,
        setHealthInsurance,
        setDate,
        setFromTime,
        setToTime,
        setMinRating,
        addSpecialty,
        addHealthInsurance,
        resetQuery,
      }}
    >
      {children}
    </DoctorQueryContext.Provider>
  );
};
