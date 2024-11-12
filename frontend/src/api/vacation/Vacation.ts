export interface Vacation {
    id: string;
    fromDate: Date;
    fromTime: string;
    toDate: Date;
    toTime: string;
    doctor: string;
}

export interface VacationQuery {
    page?: number;
    pageSize?: number;
}

export interface VacationForm {
  fromDate: Date;
  fromTime: string;
  toDate: Date;
  toTime: string;
  cancelReason?: string;
  cancelAppointments?: boolean;
}
