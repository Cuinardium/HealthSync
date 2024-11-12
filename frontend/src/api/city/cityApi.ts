import { axios } from "../axios";
import { City } from "./City";

const CITY_ENDPOINT = "/cities";

const CITY_LIST_CONTENT_TYPE = "application/vnd.city-list.v1+json";


// =============== cities ==========

export async function getCities(): Promise<City[]> {
  const allCities: City[] = [];
  let nextPageUrl: string | null = CITY_ENDPOINT;
  const initialQuery = { 
    pageSize: 50,
    sort: "popularity",
    order: "desc"
  };

  while (nextPageUrl) {
    const response = await axios.get<City[]>(nextPageUrl, {
      params: initialQuery,
      headers: {
        Accept: CITY_LIST_CONTENT_TYPE,
      },
    });

    allCities.push(...response.data);

    const linkHeader: string = response.headers.link;
    nextPageUrl = linkHeader?.match(/<([^>]+)>;\s*rel="next"/)?.[1] || null;
  }

  return allCities;
}
