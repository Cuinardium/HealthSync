import {DoctorQuery} from "../doctor/Doctor";

export interface HealthInsurance {
  name: string;
  code: string;
  popularity: number;
}

export interface HealthInsuranceQuery extends DoctorQuery{
  sort?: 'popularity' | 'standard';
  order?: 'asc' | 'desc';
}
