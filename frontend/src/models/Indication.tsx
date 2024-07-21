export class Indication {
    // Properties
    id: number;
    date: Date;
    description: string;

    // Links
    appointment: string;
    creator: string;
    file: string;
    self: string;

    constructor(
        id: number,
        date: Date,
        description: string,
        appointment: string,
        creator: string,
        file: string,
        self: string
    ) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.appointment = appointment;
        this.creator = creator;
        this.file = file;
        this.self = self;
    }

    static fromJson(json: any): Indication {
        return new Indication(
            json.id,
            json.date,
            json.description,
            json.appointment,
            json.creator,
            json.file,
            json.self
        );
    }

    static toJson(indication: Indication): any {
        return {
            id: indication.id,
            date: indication.date,
            description: indication.description,
            appointment: indication.appointment,
            creator: indication.creator,
            file: indication.file,
            self: indication.self
        };
    }
}
