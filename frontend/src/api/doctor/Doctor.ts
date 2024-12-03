import { Link } from "../link/link";
import { Day, Time } from "../time/Time";

export interface Doctor {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  address: string;
  city: string;
  rating?: number;
  ratingCount?: number;
  specialty: string;
  healthInsurances: string[];
  image?: string;
  locale: string;
  canReview: boolean;
}

export interface OccupiedHours {
  date: Date;
  hours: string[];
}

export interface OccupiedHoursResponse {
  date: string;
  hours: string[];
}

export interface AttendingHours {
  day: Day;
  hours: Time[];
}

export interface DoctorQuery {
  page?: number;
  pageSize?: number;
  name?: string;
  date?: Date;
  fromTime?: string;
  toTime?: string;
  specialty?: string[];
  city?: string[];
  healthInsurance?: string[];
  minRating?: number;
}

export interface DoctorRegisterForm {
  password: string;
  confirmPassword: string;
  name: string;
  lastname: string;
  email: string;
  healthInsurances: string[];
  city: string;
  address: string;
  specialty: string;
}

export interface DoctorEditForm {
  name: string;
  lastname: string;
  healthInsurances: string[];
  city: string;
  address: string;
  specialty: string;
  locale: string;
  image?: File;
}

export interface DoctorResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  address: string;
  city: string;
  rating?: number;
  ratingCount?: number;
  locale: string;
  links: Link[];
}
