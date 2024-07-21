export class Patient {
    firstName: string;
    lastName: string;
    healthInsurance: string;
    self: string;
    appointments: string;
    notifications: string;

    constructor(
        firstName: string,
        lastName: string,
        healthInsurance: string,
        self: string,
        appointments: string,
        notifications: string
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.healthInsurance = healthInsurance;
        this.self = self;
        this.appointments = appointments;
        this.notifications = notifications;
    }

    static fromJson(json: any): Patient {
        return new Patient(
            json.firstName,
            json.lastName,
            json.healthInsurance,
            json.self,
            json.appointments,
            json.notifications
        );
    }

    static toJson(patient: Patient): any {
        return {
            firstName: patient.firstName,
            lastName: patient.lastName,
            healthInsurance: patient.healthInsurance,
            self: patient.self,
            appointments: patient.appointments,
            notifications: patient.notifications
        };
    }
}