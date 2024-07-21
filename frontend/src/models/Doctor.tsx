import {Specialty} from "./Specialty";
import {Vacation} from "./Vacation";
import {Review} from "./Review";
import {Appointment} from "./Appointment";
import {AttendingHours} from "./AttendingHours";

export class Doctor {
    firstName: string;
    lastName: string;
    email: string;

    image: string;

    address: string;
    city: string;

    rating: number;
    ratingCount: number;

    healthInsurances: string[];
    appointments: Appointment[];
    notifications: string;

    specialty: Specialty;
    reviews: Review[];
    attendingHours: AttendingHours[];
    vacations: Vacation[];

    constructor(
        firstName: string,
        lastName: string,
        email: string,
        image: string,
        address: string,
        city: string,
        rating: number,
        ratingCount: number,
        healthInsurances: string[],
        appointments: Appointment[],
        notifications: string,
        specialty: Specialty,
        reviews: Review[],
        attendingHours: AttendingHours[],
        vacations: Vacation[]
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.image = image;
        this.address = address;
        this.city = city;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.healthInsurances = healthInsurances;
        this.appointments = appointments;
        this.notifications = notifications;
        this.specialty = specialty;
        this.reviews = reviews;
        this.attendingHours = attendingHours;
        this.vacations = vacations;
    }

    static fromJson(json: any): Doctor {
        return new Doctor(
            json.firstName,
            json.lastName,
            json.email,
            json.image,
            json.address,
            json.city,
            json.rating,
            json.ratingCount,
            json.healthInsurances,
            json.appointments,
            json.notifications,
            json.specialty,
            json.reviews,
            json.attendingHours,
            json.vacations
        );
    }

    static toJson(doctor: Doctor): any {
        return {
            firstName: doctor.firstName,
            lastName: doctor.lastName,
            email: doctor.email,
            image: doctor.image,
            address: doctor.address,
            city: doctor.city,
            rating: doctor.rating,
            ratingCount: doctor.ratingCount,
            healthInsurances: doctor.healthInsurances,
            appointments: doctor.appointments,
            notifications: doctor.notifications,
            specialty: doctor.specialty,
            reviews: doctor.reviews,
            attendingHours: doctor.attendingHours,
            vacations: doctor.vacations
        };
    }
}
