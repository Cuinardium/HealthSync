import { axios } from "../axios";
import { Vacation, VacationForm, VacationQuery } from "./Vacation";

const VACATION_ENDPOINT = (doctor_id: string) =>
  `/doctors/${doctor_id}/vacations`;

const VACATION_CONTENT_TYPE = "application/vnd.vacation.v1+json";
const VACATION_LIST_CONTENT_TYPE = "application/vnd.vacation-list.v1+json";
const VACATION_FORM_CONTENT_TYPE = "application/vnd.vacation-form.v1+json";

// =========== vacations ==============

export async function getVacations(doctorId: string, query: VacationQuery): Promise<Vacation[]> {
  const response = await axios.get<Vacation[]>(
    VACATION_ENDPOINT(doctorId),
    {
      params: query,
      headers: {
        Accept: VACATION_LIST_CONTENT_TYPE,
      },
    },
  );

  return response.data;
}

export async function createVacation(
  doctorId: string,
  vacation: VacationForm,
): Promise<Vacation> {

  (vacation as any).fromDate = vacation.fromDate.toISOString().split("T")[0];
  (vacation as any).toDate = vacation.toDate.toISOString().split("T")[0];

  const response = await axios.post<Vacation>(
    VACATION_ENDPOINT(doctorId),
    vacation,
    {
      headers: {
        "Content-Type": VACATION_FORM_CONTENT_TYPE,
      },
    },
  );

  return response.data;
}

// =========== vacations/{id} =======

export async function getVacation(doctorId: string, id: string): Promise<Vacation> {
  const response = await axios.get<Vacation>(
    `${VACATION_ENDPOINT(doctorId)}/${id}`,
    {
      headers: {
        Accept: VACATION_CONTENT_TYPE,
      },
    },
  );

  return response.data;
}

export async function deleteVacation(doctorId: string, id: string): Promise<void> {
  await axios.delete(`${VACATION_ENDPOINT(doctorId)}/${id}`);
}
