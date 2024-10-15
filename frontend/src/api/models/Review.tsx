export class Review {
    // Properties
    id: number;
    date: Date; // Using string to represent LocalDate
    description: string;
    rating: number;

    // Links
    doctor: string;
    patient: string;
    self: string;

    constructor(
        id: number,
        date: Date,
        description: string,
        rating: number,
        doctor: string,
        patient: string,
        self: string
    ) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.rating = rating;
        this.doctor = doctor;
        this.patient = patient;
        this.self = self;
    }

    static fromJson(json: any): Review {
        return new Review(
            json.id,
            json.date,
            json.description,
            json.rating,
            json.doctor,
            json.patient,
            json.self
        );
    }

    static toJson(review: Review): any {
        return {
            id: review.id,
            date: review.date,
            description: review.description,
            rating: review.rating,
            doctor: review.doctor,
            patient: review.patient,
            self: review.self
        };
    }
}
