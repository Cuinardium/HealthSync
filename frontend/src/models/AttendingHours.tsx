export class AttendingHours {
    day: string;
    hours: string[];

    constructor(day: string, hours: string[]) {
        this.day = day;
        this.hours = hours;
    }

    static fromJson(json: any): AttendingHours {
        return new AttendingHours(
            json.day,
            json.hours
        );
    }

    static toJson(AttendingHours: AttendingHours): any {
        return {
            day: AttendingHours.day,
            hours: AttendingHours.hours
        };
    }
}
