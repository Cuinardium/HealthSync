export interface Notification {
  id: number,
  userId: number,
  appointmentId: number,
}

export interface NotificationQuery {
  userId: number;
  page?: number;
  pageSize?: number;
}
