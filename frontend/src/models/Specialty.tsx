export class Specialty {
    name: string;
    code: string;
    popularity: number;

    self: string;

    constructor(name: string, code: string, popularity: number, self: string) {
        this.name = name;
        this.code = code;
        this.popularity = popularity;
        this.self = self;
    }

    static fromJson(json: any): Specialty {
        return new Specialty(
            json.name,
            json.code,
            json.popularity,
            json.self
        );
    }

    static toJson(specialty: Specialty): any {
        return {
            name: specialty.name,
            code: specialty.code,
            popularity: specialty.popularity,
            self: specialty.self
        };
    }
}
