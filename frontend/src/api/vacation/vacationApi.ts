import { axios } from "../axios";
import { getPage, Page } from "../page/Page";
import { Vacation, VacationForm, VacationQuery } from "./Vacation";

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
  const response = await axios.get<Vacation[]>(VACATION_ENDPOINT(doctorId), {
    params: query,
    headers: {
      Accept: VACATION_LIST_CONTENT_TYPE,
    },
  });

  if (response.status == 200) {
    // Set fromDate and toDate to Date object
    response.data = response.data?.map((review) => mapDates(review));
  }

  return getPage(response);
}

export async function createVacation(
  doctorId: string,
  vacation: VacationForm,
): Promise<Vacation> {
  const body = {
    ...vacation,
    fromDate: vacation.fromDate.toISOString().split("T")[0],
    toDate: vacation.toDate.toISOString().split("T")[0],
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
  const response = await axios.get<Vacation>(
    `${VACATION_ENDPOINT(doctorId)}/${id}`,
    {
      headers: {
        Accept: VACATION_CONTENT_TYPE,
      },
    },
  );

  return mapDates(response.data);
}

export async function deleteVacation(
  doctorId: string,
  id: string,
): Promise<void> {
  await axios.delete(`${VACATION_ENDPOINT(doctorId)}/${id}`);
}

// ======== auxiliary functions =========

function mapDates(vacation: Vacation): Vacation {
  vacation.fromDate = new Date(vacation.fromDate);
  vacation.toDate = new Date(vacation.toDate);
  return vacation;
}
