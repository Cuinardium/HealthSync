export interface HealthInsurance {
  name: string;
  code: string;
  popularity: number;
}

export interface HealthInsuranceQuery {
  sort?: 'popularity' | 'standard';
  order?: 'asc' | 'desc';
}
