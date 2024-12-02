import { Link } from "../link/link";

export interface Review {
    id: number;
    date: Date;
    description: string;
    rating: number;
    patientName: string;
    patientImage?: string;
}

export interface ReviewForm {
    description: string;
    rating: number;
}

export interface ReviewQuery {
    page?: number;
    pageSize?: number;
}

export interface ReviewResponse {
    date: string;
    description: string;
    id: number;
    rating: number;
    patientName: string;
    links: Link[];
}
