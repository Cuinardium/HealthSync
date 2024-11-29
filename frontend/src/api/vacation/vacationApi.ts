import { axios } from "../axios";
import { getPage, Page } from "../page/Page";
import {
  Vacation,
  VacationForm,
  VacationQuery,
  VacationResponse,
} from "./Vacation";
import { formatDate, parseLocalDate } from "../util/dateUtils";
import {Time, TIMES} from "../time/Time";

const VACATION_ENDPOINT = (doctor_id: string) =>
  `/doctors/${doctor_id}/vacations`;

const VACATION_CONTENT_TYPE = "application/vnd.vacation.v1+json";
const VACATION_LIST_CONTENT_TYPE = "application/vnd.vacation-list.v1+json";
const VACATION_FORM_CONTENT_TYPE = "application/vnd.vacation-form.v1+json";

// =========== vacations ==============

export async function getVacations(
  doctorId: string,
  query: VacationQuery,
): Promise<Page<Vacation>> {
  const response = await axios.get(VACATION_ENDPOINT(doctorId), {
    params: query,
    headers: {
      Accept: VACATION_LIST_CONTENT_TYPE,
    },
  });

  if (response.status === 200) {
    // Set fromDate and toDate to Date object
    response.data = response.data?.map((vacation: VacationResponse) =>
      mapValues(vacation),
    );
  }

  return getPage(response);
}

export async function createVacation(
  doctorId: string,
  vacation: VacationForm,
): Promise<Vacation> {
  const body = {
    ...vacation,
    fromDate: formatDate(vacation.fromDate),
    toDate: formatDate(vacation.toDate),
  };

  const response = await axios.post<Vacation>(
    VACATION_ENDPOINT(doctorId),
    body,
    {
      headers: {
        "Content-Type": VACATION_FORM_CONTENT_TYPE,
      },
    },
  );

  const location = response.headers.location;

  const vacationId = location?.split("/").pop();
  return await getVacation(doctorId, vacationId as string);
}

// =========== vacations/{id} =======

export async function getVacation(
  doctorId: string,
  id: string,
): Promise<Vacation> {
  const response = await axios.get<VacationResponse>(
    `${VACATION_ENDPOINT(doctorId)}/${id}`,
    {
      headers: {
        Accept: VACATION_CONTENT_TYPE,
      },
    },
  );

  return mapValues(response.data);
}

export async function deleteVacation(
  doctorId: string,
  id: string,
): Promise<void> {
  await axios.delete(`${VACATION_ENDPOINT(doctorId)}/${id}`);
}

// ======== auxiliary functions =========

function isHappening(
  fromDate: Date,
  toDate: Date,
  fromTime: string,
  toTime: string,
): boolean {
  const today = new Date();
  const currentTimeIndex =
    today.getHours() * 2 + Math.floor(today.getMinutes() / 30);
  const fromTimeIndex = TIMES.indexOf(fromTime as Time);
  const toTimeIndex = TIMES.indexOf(toTime as Time);

  // If today is between non-inclusive fromDate and toDate
  if (today >= fromDate && today < toDate) {
    return true;
  }

  // If today is the fromDate and current time is after fromTime
  if (today === fromDate && currentTimeIndex >= fromTimeIndex) {
    return true;
  }

  // If today is the toDate and current time is before toTime
  return today === toDate && currentTimeIndex <= toTimeIndex;
}

function isPast(fromDate: Date, fromTime: string): boolean {
  const today = new Date();
  const currentTimeIndex =
    today.getHours() * 2 + Math.floor(today.getMinutes() / 30);
  const fromTimeIndex = TIMES.indexOf(fromTime as Time);

  return today > fromDate || (today === fromDate && currentTimeIndex > fromTimeIndex);
}

function mapValues(vacation: VacationResponse): Vacation {
  const fromDate = parseLocalDate(vacation.fromDate);
  const toDate = parseLocalDate(vacation.toDate);
  const isVacationHappening = isHappening(fromDate, toDate, vacation.fromTime, vacation.toTime);
    const isVacationPast = isPast(fromDate, vacation.fromTime);
  return {
    ...vacation,
    fromDate,
    toDate,
    isHappening: isVacationHappening,
    isPast: !isVacationHappening && isVacationPast,
  };
}
