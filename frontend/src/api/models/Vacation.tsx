export class Vacation {
    fromDate: Date;
    fromTime: string;
    toDate: Date;
    toTime: string;

    doctor: string;
    self: string;

    constructor(
        fromDate: Date,
        fromTime: string,
        toDate: Date,
        toTime: string,
        doctor: string,
        self: string
    ) {
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.doctor = doctor;
        this.self = self;
    }

    static fromJson(json: any): Vacation {
        return new Vacation(
            json.fromDate,
            json.fromTime,
            json.toDate,
            json.toTime,
            json.doctor,
            json.self
        );
    }

    static toJson(vacation: Vacation): any {
        return {
            fromDate: vacation.fromDate,
            fromTime: vacation.fromTime,
            toDate: vacation.toDate,
            toTime: vacation.toTime,
            doctor: vacation.doctor,
            self: vacation.self
        };
    }
}
