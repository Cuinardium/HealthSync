import {
  keepPreviousData,
  useInfiniteQuery,
  useMutation,
  useQuery,
  useQueryClient,
} from "@tanstack/react-query";
import {
  createDoctor,
  getDoctorAttendingHours,
  getDoctorById,
  getDoctorOccupiedHours,
  getDoctors,
  updateDoctor,
  updateDoctorAttendingHours,
} from "../api/doctor/doctorApi";
import {
  AttendingHours,
  Doctor,
  DoctorEditForm,
  DoctorQuery,
  DoctorRegisterForm,
  DoctorResponse,
  OccupiedHours,
} from "../api/doctor/Doctor";

import { queryClient } from "../api/queryClient";
import { AxiosError } from "axios";
import { getHealthInsurance } from "../api/health-insurance/healthInsuranceApi";
import { getSpecialty } from "../api/specialty/specialtyApi";
import { HealthInsurance } from "../api/health-insurance/HealthInsurance";
import { Specialty } from "../api/specialty/Specialty";
import { useMemo } from "react";
import { Day, DAYS, Time, TIMES } from "../api/time/Time";
import { formatDate, parseLocalDate } from "../api/util/dateUtils";
import { Appointment } from "../api/appointment/Appointment";
import { getAllConfirmedAppointmentsInRange } from "../api/appointment/appointmentsApi";
import {parseLocale} from "../api/locale/locale";

const STALE_TIME = 5 * 60 * 1000;

// =========== useDoctors ===========

export function useDoctors(query: DoctorQuery) {
  return useInfiniteQuery(
    {
      queryKey: ["doctors", query],
      queryFn: async ({ pageParam = 1 }) => {
        const doctorResponsePage = await getDoctors({
          ...query,
          page: pageParam,
        });

        // Add health insurances and specialty to each doctor
        const doctors = await Promise.all(
          doctorResponsePage.content.map(async (doctor) =>
            mapDoctorDetails(doctor),
          ),
        );

        return {
          ...doctorResponsePage,
          content: doctors,
        };
      },
      staleTime: STALE_TIME,
      getNextPageParam: (lastPage) =>
        lastPage.currentPage < lastPage.totalPages
          ? lastPage.currentPage + 1
          : undefined,
      initialPageParam: 1,
    },
    queryClient,
  );
}

// =========== useDoctor ===========

export function useDoctor(doctorId: string) {
  return useQuery<Doctor, AxiosError>(
    {
      queryKey: ["doctor", doctorId],
      queryFn: async () => {
        const doctorResp = await getDoctorById(doctorId);
        return mapDoctorDetails(doctorResp);
      },
      enabled: !!doctorId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// ========== useUpdateDoctor ==========

export function useUpdateDoctor(
  id: string,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<DoctorEditForm, AxiosError, DoctorEditForm>(
    {
      mutationFn: (doctor: DoctorEditForm) => updateDoctor(id, doctor),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["doctor", id],
        });
        onSuccess();
      },
      onError: (error) => {
        onError(error);
      },
    },
    queryClient,
  );
}

// ========== useCreateDoctor ==========

export function useCreateDoctor(
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<void, AxiosError, DoctorRegisterForm>(
    {
      mutationFn: (doctor: DoctorRegisterForm) => createDoctor(doctor),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["doctors"],
        });
        onSuccess();
      },
      onError: (error) => {
        onError(error);
      },
    },
    queryClient,
  );
}

// =========== useAttendingHours ===========

export function useAttendingHours(doctorId: string) {
  return useQuery<AttendingHours[], Error>(
    {
      queryKey: ["attendingHours", doctorId],
      queryFn: () => getDoctorAttendingHours(doctorId),
      enabled: !!doctorId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// =========== useUpdateAttendingHours ===========

export function useUpdateAttendingHours(
  doctorId: string,
  onSuccess: () => void,
  onError: (error: AxiosError) => void,
) {
  return useMutation<AttendingHours[], AxiosError, AttendingHours[]>(
    {
      mutationFn: (attendingHours: AttendingHours[]) =>
        updateDoctorAttendingHours(doctorId, attendingHours),
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["attendingHours", doctorId],
        });
        onSuccess();
      },
      onError: (error) => {
        onError(error);
      },
    },
    queryClient,
  );
}

// =========== useOccupiedHours ===========

export function useOccupiedHours(doctorId: string, from: Date, to: Date) {
  return useQuery<OccupiedHours[], Error>(
    {
      queryKey: ["occupiedHours", doctorId, from, to],
      queryFn: () => getDoctorOccupiedHours(doctorId, from, to),
      enabled: !!doctorId,
      staleTime: STALE_TIME,
    },
    queryClient,
  );
}

// =========== useAvailableHours ===========

type AvailableHoursMap = Record<string, string[]>;

const generateDateRange = (start: Date, end: Date): Date[] => {
  const dates: Date[] = [];
  let current = new Date(start);
  while (current <= end) {
    dates.push(new Date(current));
    current.setDate(current.getDate() + 1);
  }
  return dates;
};

async function fetchAvailableHours(
  doctorId: string,
  from: Date,
  to: Date,
  patientId?: string,
): Promise<AvailableHoursMap> {
  let patientAppointments: Appointment[] = [];
  if (patientId) {
    patientAppointments = await getAllConfirmedAppointmentsInRange(
      from,
      to,
      patientId,
    );
  }

  let attendingHours = queryClient.getQueryData<AttendingHours[]>([
    "attendingHours",
    doctorId,
  ]);
  if (!attendingHours) {
    attendingHours = await getDoctorAttendingHours(doctorId);
    queryClient.setQueryData(["attendingHours", doctorId], attendingHours);
  }

  const occupiedHours = await getDoctorOccupiedHours(doctorId, from, to);

  const attendingMap: Record<Day, string[]> = attendingHours.reduce(
    (map, entry) => {
      map[entry.day] = entry.hours;
      return map;
    },
    {} as Record<Day, string[]>,
  );

  const availableHoursMap: AvailableHoursMap = {};

  const datesInRange = generateDateRange(from, to);

  datesInRange.forEach((date) => {
    let dayIndex = (date.getDay() - 1) % DAYS.length;
    if (dayIndex < 0) {
      dayIndex = DAYS.length - 1;
    }
    const dayOfWeek = DAYS[dayIndex]; // Get day name
    const attendingHoursForDay = attendingMap[dayOfWeek] || [];

    const dateKey = formatDate(date);

    // Agrego las horas disponibles default
    availableHoursMap[dateKey] = [...attendingHoursForDay];
  });

  // Saco las horas ocupadas por vacaciones o turnos
  occupiedHours.forEach((entry) => {
    const dateKey = formatDate(entry.date);
    if (availableHoursMap[dateKey]) {
      availableHoursMap[dateKey] = availableHoursMap[dateKey].filter(
        (hour) => !entry.hours.includes(hour),
      );
    }
  });

  // Saco las horas ocupadas por el paciente
  patientAppointments.forEach((appointment) => {
    const dateKey = formatDate(appointment.date);
    if (availableHoursMap[dateKey]) {
      availableHoursMap[dateKey] = availableHoursMap[dateKey].filter(
        (hour) => appointment.timeBlock !== hour,
      );
    }
  });

  // Saco las horas que ya pasaron
  const today = new Date();
  const currentTime =
    today.getHours() * 2 + Math.floor(today.getMinutes() / 30);
  const todayKey = formatDate(today);

  if (availableHoursMap[todayKey]) {
    availableHoursMap[todayKey] = availableHoursMap[todayKey].filter((hour) => {
      const hourIndex = TIMES.indexOf(hour as Time);
      return hourIndex > currentTime;
    });
  }

  return availableHoursMap;
}

export function useAvailableHours(
  doctorId: string,
  from: Date,
  to: Date,
  patientId?: string,
) {
  return useQuery<AvailableHoursMap, Error>({
    queryKey: ["availableHours", doctorId, from, to, patientId ?? "anonymous"],
    queryFn: () => fetchAvailableHours(doctorId, from, to, patientId),
    enabled: !!doctorId,
    staleTime: 1000 * 60 * 5,
  });
} // ========= Utility Functions =========

async function mapDoctorDetails(doctorResp: DoctorResponse): Promise<Doctor> {
  // Fetch specialty
  const specialtyLink = doctorResp.links.find(
    (link) => link.rel === "specialty",
  );
  if (!specialtyLink) {
    throw new Error("Specialty link not found");
  }
  const specialtyId = specialtyLink.href.split("/").pop();
  let specialtyResp: Specialty;
  const specialtyCacheKey = ["specialty", specialtyId];

  if (queryClient.getQueryData(specialtyCacheKey)) {
    specialtyResp = queryClient.getQueryData(specialtyCacheKey) as Specialty;
  } else {
    specialtyResp = await getSpecialty(specialtyId as string);
    queryClient.setQueryData(specialtyCacheKey, specialtyResp);
  }

  const specialty = specialtyResp.code.toLowerCase().replace(/_/g, ".");

  // Fetch health insurances
  const healthInsuranceLinks = doctorResp.links.filter(
    (link) => link.rel === "healthinsurance",
  );

  const healthInsurances = await Promise.all(
    healthInsuranceLinks.map(async (link) => {
      const healthInsuranceId = link.href.split("/").pop();
      const healthInsuranceCacheKey = ["healthInsurance", healthInsuranceId];

      let healthInsuranceResp: HealthInsurance;

      if (queryClient.getQueryData(healthInsuranceCacheKey)) {
        healthInsuranceResp = queryClient.getQueryData(
          healthInsuranceCacheKey,
        ) as HealthInsurance;
      } else {
        healthInsuranceResp = await getHealthInsurance(
          healthInsuranceId as string,
        );
        queryClient.setQueryData(healthInsuranceCacheKey, healthInsuranceResp);
      }

      return healthInsuranceResp.code.toLowerCase().replace(/_/g, ".");
    }),
  );

  // Check if doctor can be reviewed
  const canReview = doctorResp.links.some(
    (link) => link.rel === "create-review",
  );

  // Get doctor's image
  const image = doctorResp.links.find((link) => link.rel === "image")?.href;

  // Parse locale for only the language
  const locale = parseLocale(doctorResp.locale);

  return {
    ...doctorResp,
    specialty,
    healthInsurances,
    canReview,
    image,
    locale,
  };
}
