export interface Vacation {
    id: string;
    fromDate: Date;
    fromTime: string;
    toDate: Date;
    toTime: string;
}

export interface VacationResponse {
    id: string;
    fromDate: string;
    fromTime: string;
    toDate: string;
    toTime: string;
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
