export class Vacation {
    id: number;
    fromDate: Date;
    fromTime: string;
    toDate: Date;
    toTime: string;

    doctor: string;
    self: string;

    constructor(
        id: number,
        fromDate: Date,
        fromTime: string,
        toDate: Date,
        toTime: string,
        doctor: string,
        self: string
    ) {
        this.id = id;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.doctor = doctor;
        this.self = self;
    }

    static fromJson(json: any): Vacation {
        return new Vacation(
            json.id,
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
            id: vacation.id,
            fromDate: vacation.fromDate,
            fromTime: vacation.fromTime,
            toDate: vacation.toDate,
            toTime: vacation.toTime,
            doctor: vacation.doctor,
            self: vacation.self
        };
    }
}
