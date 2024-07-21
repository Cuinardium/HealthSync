export class City {
    name: string;
    popularity: number;

    constructor(name: string, popularity: number) {
        this.name = name;
        this.popularity = popularity;
    }

    static fromJson(json: any): City {
        return new City(
            json.name,
            json.popularity
        );
    }

    static toJson(city: City): any {
        return {
            name: city.name,
            popularity: city.popularity
        };
    }

    setName(name: string): void {
        this.name = name;
    }

    // Similarly, add other setter methods if needed
}
