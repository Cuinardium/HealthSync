import { axios } from "../axios";
import { HealthInsurance, HealthInsuranceQuery } from "./HealthInsurance";
import {formatDate} from "../util/dateUtils";
import {fetchAllPaginatedData} from "../page/Page";

const HEALTH_INSURANCE_ENDPOINT = "healthinsurances";

const HEALTH_INSURANCE_CONTENT_TYPE = "application/vnd.health-insurance.v1+json";
const HEALTH_INSURANCE_LIST_CONTENT_TYPE = "application/vnd.health-insurance-list.v1+json";

// =========== healthinsurances ==============

export async function getHealthInsurances(query: HealthInsuranceQuery): Promise<HealthInsurance[]> {
  const dateStr = query.date ? formatDate(query.date) : undefined;

  const initialQuery = {
    ...query,
    pageSize: 50,
    date: dateStr,
  };

  return fetchAllPaginatedData<HealthInsurance>(
      HEALTH_INSURANCE_ENDPOINT,
      initialQuery,
      { Accept: HEALTH_INSURANCE_LIST_CONTENT_TYPE },
  );
}


// =========== healthinsurances/{id} =======

export async function getHealthInsurance(id: String): Promise<HealthInsurance> {
  const response = await axios.get<HealthInsurance>(
    `${HEALTH_INSURANCE_ENDPOINT}/${id}`,
    {
      headers: {
        Accept: HEALTH_INSURANCE_CONTENT_TYPE,
      },
    },
  );

  return response.data;
}
