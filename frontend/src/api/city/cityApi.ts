import {City, CityQuery} from "./City";
import {formatDate} from "../util/dateUtils";
import {fetchAllPaginatedData} from "../page/Page";

const CITY_ENDPOINT = "/cities";

const CITY_LIST_CONTENT_TYPE = "application/vnd.city-list.v1+json";


// =============== cities ==========

export async function getCities(query: CityQuery): Promise<City[]> {
  const dateStr = query.date ? formatDate(query.date) : undefined;

  const initialQuery = {
    ...query,
    pageSize: 50,
    date: dateStr,
  };

  return fetchAllPaginatedData<City>(
      CITY_ENDPOINT,
      initialQuery,
      { Accept: CITY_LIST_CONTENT_TYPE },
  );
}

