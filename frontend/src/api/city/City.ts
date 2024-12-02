import {DoctorQuery} from "../doctor/Doctor";

export interface City {
    name: string;
    popularity: number;
}


export interface CityQuery extends DoctorQuery {
    page?: number;
    pageSize?: number;
    sort?: "popularity" | "alphabetical";
    order?: "asc" | "desc";
}
