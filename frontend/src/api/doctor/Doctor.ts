export interface Doctor {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    address: string;
    city: string;
    rating: number;
    ratingCount: number;
    specialty: string;
    healthInsurances: string[];
}

export interface OccupiedHours {
  date: Date;
  hours: string[];
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
