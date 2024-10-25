import { axios } from "../axios";
import { Specialty, SpecialtyQuery } from "./Specialty";

const SPECIALTY_ENDPOINT = "specialties";

const SPECIALTY_CONTENT_TYPE = "application/vnd.specialty.v1+json";
const SPECIALTY_LIST_CONTENT_TYPE = "application/vnd.specialty-list.v1+json";

// =========== specialties ==============

export async function getSpecialties(query: SpecialtyQuery): Promise<Specialty[]> {
  const allSpecialties: Specialty[] = [];
  let nextPageUrl: string | null = SPECIALTY_ENDPOINT;

  const initialQuery = {
    pageSize: 100,
    ...query
  };

  while (nextPageUrl) {
    const response = await axios.get<Specialty[]>(nextPageUrl, {
      params: initialQuery,
      headers: {
        Accept: SPECIALTY_LIST_CONTENT_TYPE,
      },
    });

    allSpecialties.push(...response.data);

    const linkHeader: string = response.headers.link;
    nextPageUrl = linkHeader?.match(/<([^>]+)>;\s*rel="next"/)?.[1] || null;
  }

  return allSpecialties;
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
