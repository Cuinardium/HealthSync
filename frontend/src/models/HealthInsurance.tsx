export class HealthInsuranceDto {
    name: string;
    code: string;

    self: string;

    constructor(name: string, code: string, self: string) {
        this.name = name;
        this.code = code;
        this.self = self;
    }

    static fromJson(json: any): HealthInsuranceDto {
        return new HealthInsuranceDto(
            json.name,
            json.code,
            json.self
        );
    }

    static toJson(healthInsurance: HealthInsuranceDto): any {
        return {
            name: healthInsurance.name,
            code: healthInsurance.code,
            self: healthInsurance.self
        };
    }
}
