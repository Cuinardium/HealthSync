export interface Appointment {
  date: Date;
  description: string;
  cancelDescription: string | null;
  doctor: string;
  id: number;
  patient: string;
  self: string;
  status: "CONFIRMED" | "CANCELLED" | "COMPLETED";
  timeBlock: string;
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
}
