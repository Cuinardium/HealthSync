export interface Review {
    id: number;
    date: Date;
    description: string;
    rating: number;
    doctor: string;
    patient: string;
}

export interface ReviewForm {
    description: string;
    rating: number;
}

export interface ReviewQuery {
    page?: number;
    pageSize?: number;
}
