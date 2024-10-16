import { axios } from "../axios";
import { City, CityQuery } from "./City";

const CITY_ENDPOINT = "/cities";

const CITY_LIST_CONTENT_TYPE = "application/vnd.city-list.v1+json";


// =============== cities ==========

export async function getCities(query: CityQuery): Promise<City[]> {
  const response = await axios.get(CITY_ENDPOINT, {
    params: query,
    headers: {
      Accept: CITY_LIST_CONTENT_TYPE,
    },
  });

  return response.data;
}



