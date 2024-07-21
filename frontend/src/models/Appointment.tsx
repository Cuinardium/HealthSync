export class Appointment {
    // Properties
    id: number;
    date: Date; // Using string to represent LocalDate
    timeBlock: string;
    status: string;
    description: string;
    cancelDescription: string;

    // Links
    doctor: string;
    patient: string;
    self: string;

    constructor(
        id: number,
        date: Date,
        timeBlock: string,
        status: string,
        description: string,
        cancelDescription: string,
        doctor: string,
        patient: string,
        self: string
    ) {
        this.id = id;
        this.date = date;
        this.timeBlock = timeBlock;
        this.status = status;
        this.description = description;
        this.cancelDescription = cancelDescription;
        this.doctor = doctor;
        this.patient = patient;
        this.self = self;
    }

    static fromJson(json: any): Appointment {
        return new Appointment(
            json.id,
            json.date,
            json.timeBlock,
            json.status,
            json.description,
            json.cancelDescription,
            json.doctor,
            json.patient,
            json.self
        );
    }

    static toJson(appointment: Appointment): any {
        return {
            id: appointment.id,
            date: appointment.date,
            timeBlock: appointment.timeBlock,
            status: appointment.status,
            description: appointment.description,
            cancelDescription: appointment.cancelDescription,
            doctor: appointment.doctor,
            patient: appointment.patient,
            self: appointment.self
        };
    }
}
