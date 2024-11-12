export interface City {
    name: string;
    popularity: number;
}


export interface CityQuery {
    page?: number;
    pageSize?: number;
    sort?: "popularity" | "alphabetical";
    order?: "asc" | "desc";
}
