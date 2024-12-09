import { axios } from "../axios";
import { Specialty, SpecialtyQuery } from "./Specialty";
import { formatDate } from "../util/dateUtils";
import {fetchAllPaginatedData} from "../page/Page";

const SPECIALTY_ENDPOINT = "specialties";

const SPECIALTY_CONTENT_TYPE = "application/vnd.specialty.v1+json";
const SPECIALTY_LIST_CONTENT_TYPE = "application/vnd.specialty-list.v1+json";

// =========== specialties ==============

export async function getSpecialties(query: SpecialtyQuery): Promise<Specialty[]> {
  const dateStr = query.date ? formatDate(query.date) : undefined;

  const initialQuery = {
    ...query,
    pageSize: 100,
    date: dateStr,
  };

  return fetchAllPaginatedData<Specialty>(
      SPECIALTY_ENDPOINT,
      initialQuery,
      { Accept: SPECIALTY_LIST_CONTENT_TYPE },
  );
}

export async function getPopularSpecialties(query : SpecialtyQuery) : Promise<Specialty[]> {
  const initialQuery = {
    ...query,
    page: 1,
    sort: "popularity",
    order: "desc",
  };

  const response = await axios.get<Specialty[]>(SPECIALTY_ENDPOINT, {
    params: initialQuery,
    headers: { Accept: SPECIALTY_LIST_CONTENT_TYPE },
  });

  return response.data.filter((specialty) => specialty.popularity > 0);
}


// =========== specialties/{id} =======

export async function getSpecialty(id: String): Promise<Specialty> {
  const response = await axios.get<Specialty>(`${SPECIALTY_ENDPOINT}/${id}`, {
    headers: {
      Accept: SPECIALTY_CONTENT_TYPE,
    },
  });

  return response.data;
}
