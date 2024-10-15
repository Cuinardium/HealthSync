import { axios } from "../axios";
import { HealthInsurance, HealthInsuranceQuery } from "./HealthInsurance";

const HEALTH_INSURANCE_ENDPOINT = "healthinsurances";

const HEALTH_INSURANCE_CONTENT_TYPE = "application/vnd.health-insurance.v1+json";
const HEALTH_INSURANCE_LIST_CONTENT_TYPE = "application/vnd.health-insurance-list.v1+json";

// =========== healthinsurances ==============

export async function getHealthInsurances(
  query: HealthInsuranceQuery = {},
): Promise<HealthInsurance[]> {
  const response = await axios.get<HealthInsurance[]>(HEALTH_INSURANCE_ENDPOINT, {
    params: query,
    headers: {
      Accept: HEALTH_INSURANCE_LIST_CONTENT_TYPE,
    },
  });

  return response.data;
}

// =========== healthinsurances/{id} =======

export async function getHealthInsurance(id: String): Promise<HealthInsurance> {
  const response = await axios.get<HealthInsurance>(`${HEALTH_INSURANCE_ENDPOINT}/${id}`, {
    headers: {
      Accept: HEALTH_INSURANCE_CONTENT_TYPE,
    },
  });

  return response.data;
}
