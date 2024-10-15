export interface HealthInsurance {
  name: string;
  code: string;
  self: string;
}

export interface HealthInsuranceQuery {
  page?: number;
  pageSize?: number;
}
