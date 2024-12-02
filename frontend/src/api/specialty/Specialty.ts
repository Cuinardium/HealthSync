import {DoctorQuery} from "../doctor/Doctor";

export interface Specialty {
  name: string;
  code: string;
  popularity: number;
}

export interface SpecialtyQuery extends DoctorQuery{
  sort?: 'popularity' | 'standard';
  order?: 'asc' | 'desc';
}
