import { axios } from "../axios";
import { HealthInsurance } from "./HealthInsurance";

const HEALTH_INSURANCE_ENDPOINT = "healthinsurances";

const HEALTH_INSURANCE_CONTENT_TYPE = "application/vnd.health-insurance.v1+json";
const HEALTH_INSURANCE_LIST_CONTENT_TYPE = "application/vnd.health-insurance-list.v1+json";

// =========== healthinsurances ==============

export async function getHealthInsurances(): Promise<HealthInsurance[]> {
  const allHealthInsurances: HealthInsurance[] = [];
  let nextPageUrl: string | null = HEALTH_INSURANCE_ENDPOINT;

  const initialQuery = {
    pageSize: 50,
  };

  while (nextPageUrl) {
    const response = await axios.get<HealthInsurance[]>(nextPageUrl, {
      params: initialQuery,
      headers: {
        Accept: HEALTH_INSURANCE_LIST_CONTENT_TYPE,
      },
    });

    allHealthInsurances.push(...response.data);

    const linkHeader: string = response.headers.link;
    nextPageUrl = linkHeader?.match(/<([^>]+)>;\s*rel="next"/)?.[1] || null;
  }

  return allHealthInsurances;
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
