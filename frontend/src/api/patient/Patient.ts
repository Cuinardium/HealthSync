import { Link } from "../link/link";

export interface Patient {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  healthInsurance: string;
  image?: string;
  locale: string;
}

export interface PatientRegisterForm {
  password: string;
  confirmPassword: string;
  name: string;
  lastname: string;
  email: string;
  healthInsurance: string;
}

export interface PatientEditForm {
  name: string;
  lastname: string;
  healthInsurance: string;
  locale: string;
  image?: File;
}

export interface PatientResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  locale: string;
  links: Link[];
}
