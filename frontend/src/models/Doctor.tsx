export class Doctor {
    firstName: string;
    lastName: string;
    address: string;
    city: string;
    healthInsurances: string;
    attendingHours: string;
    appointments: string;
    reviews: string;
    notifications: string;
    image: string;
    self: string;

    constructor(
        firstName: string,
        lastName: string,
        address: string,
        city: string,
        healthInsurances: string,
        attendingHours: string,
        appointments: string,
        reviews: string,
        notifications: string,
        image: string,
        self: string
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.healthInsurances = healthInsurances;
        this.attendingHours = attendingHours;
        this.appointments = appointments;
        this.reviews = reviews;
        this.notifications = notifications;
        this.image = image;
        this.self = self;
    }

    static fromJson(json: any): Doctor {
        return new Doctor(
            json.firstName,
            json.lastName,
            json.address,
            json.city,
            json.healthInsurances,
            json.attendingHours,
            json.appointments,
            json.reviews,
            json.notifications,
            json.image,
            json.self
        );
    }

    static toJson(doctor: Doctor): any {
        return {
            firstName: doctor.firstName,
            lastName: doctor.lastName,
            address: doctor.address,
            city: doctor.city,
            healthInsurances: doctor.healthInsurances,
            attendingHours: doctor.attendingHours,
            appointments: doctor.appointments,
            reviews: doctor.reviews,
            notifications: doctor.notifications,
            image: doctor.image,
            self: doctor.self
        };
    }
}
