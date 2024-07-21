export class Notification {
    id: number;
    userId: number;
    appointmentId: number;

    self: string;


    constructor(id: number, userId: number, appointmentId: number, self: string) {
        this.id = id;
        this.userId = userId;
        this.appointmentId = appointmentId;
        this.self = self;
    }

    static fromJson(json: any): Notification {
        return new Notification(
            json.id,
            json.userId,
            json.appointmentId,
            json.self
        );
    }

    static toJson(notification: Notification): any {
        return {
            id: notification.id,
            userId: notification.userId,
            appointmentId: notification.appointmentId,
            self: notification.self
        };
    }
}
