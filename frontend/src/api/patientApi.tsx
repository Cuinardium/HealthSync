import api from "./api";
import {Patient} from "../models/Patient";

const patientEndpoint = '/patients'


//TODO revisar tema headers
export async function createPatient(patient: Patient): Promise<Patient>{
    const response = await api.post(patientEndpoint, Patient.toJson(patient));
    return response.data;
}

export async function getPatientById(id : String): Promise<Patient>{
    const response = await api.get(`${patientEndpoint}/${id}`);
    return Patient.fromJson(response.data)
}