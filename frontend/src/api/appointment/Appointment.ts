import { Link } from "../link/link";

export interface Appointment {
  date: Date;
  description: string;
  cancelDescription: string | null;
  doctorId: string;
  id: number;
  patientId: string;
  status: "CONFIRMED" | "CANCELLED" | "COMPLETED";
  timeBlock: string;
  canIndicate: boolean,
  canCancel: boolean,
}

export interface CancelAppointmentForm {
  status: "CANCELLED";
  description: string;
}

export interface AppointmentForm {
  date: Date;
  timeBlock: string;
  description: string;
  doctorId: number;
}

export interface AppointmentQuery {
  userId: string;
  page?: number;
  pageSize?: number;
  status?: "CONFIRMED" | "CANCELLED" | "COMPLETED";
  order?: "asc" | "desc";
  date?: Date;
}

export interface AppointmentResponse {
  date: string;
  description: string;
  cancelDescription: string | null;
  id: number;
  status: "CONFIRMED" | "CANCELLED" | "COMPLETED";
  timeBlock: string;
  links: Link[];
}
