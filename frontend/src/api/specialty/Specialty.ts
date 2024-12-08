import {DoctorQuery} from "../doctor/Doctor";
import {Link} from "../link/link";

export interface Specialty {
  name: string;
  code: string;
  popularity: number;
}

export interface SpecialtyQuery extends DoctorQuery{
  sort?: 'popularity' | 'standard';
  order?: 'asc' | 'desc';
}
