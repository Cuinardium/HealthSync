export interface Patient {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    healthInsurance: string;
    image: string;
}

export interface PatientRegisterForm {
    password: string;
    confirmPassword: string;
    name: string;
    lastname: string;
    email: string;
    healthInsurance: string;
}

export interface PatientEditForm {
    name: string;
    lastname: string;
    email: string;
    healthInsurance: string;
    locale: string;
    image?: File;
}
