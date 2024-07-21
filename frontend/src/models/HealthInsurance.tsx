export class HealthInsurance {
    name: string;
    code: string;

    self: string;

    constructor(name: string, code: string, self: string) {
        this.name = name;
        this.code = code;
        this.self = self;
    }

    static fromJson(json: any): HealthInsurance {
        return new HealthInsurance(
            json.name,
            json.code,
            json.self
        );
    }

    static toJson(healthInsurance: HealthInsurance): any {
        return {
            name: healthInsurance.name,
            code: healthInsurance.code,
            self: healthInsurance.self
        };
    }
}
