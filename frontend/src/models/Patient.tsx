import {Notification} from "./Notification";
import {Appointment} from "./Appointment";
import {HealthInsurance} from "./HealthInsurance";

export class Patient {
    firstName: string;
    lastName: string;
    email: string;

    image: string;

    healthInsurance: HealthInsurance;
    appointments: Appointment[];
    notifications: Notification[];

    self: string;

    constructor(
        firstName: string,
        lastName: string,
        email: string,
        image: string,
        healthInsurance: HealthInsurance,
        appointments: Appointment[],
        notifications: Notification[],
        self: string
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.healthInsurance = healthInsurance;
        this.appointments = appointments;
        this.notifications = notifications;
        this.self = self;
    }

    static fromJson(json: any): Patient {
        return new Patient(
            json.firstName,
            json.lastName,
            json.email,
            json.image,
            json.healthInsurance,
            json.appointments,
            json.notifications,
            json.self
        );
    }

    static toJson(patient: Patient): any {
        return {
            firstName: patient.firstName,
            lastName: patient.lastName,
            email: patient.email,
            image: patient.image,
            healthInsurance: patient.healthInsurance,
            appointments: patient.appointments,
            notifications: patient.notifications,
            self: patient.self
        };
    }
}
