import {axios} from "../axios";
import {Patient} from "./Patient";

const patientEndpoint = '/patients'


//TODO revisar tema headers
export async function createPatient(patient: Patient): Promise<Patient>{
    const response = await axios.post(patientEndpoint, Patient.toJson(patient));
    return response.data;
}

export async function getPatientById(id : String): Promise<Patient>{
    const response = await axios.get(`${patientEndpoint}/${id}`);
    return Patient.fromJson(response.data)
}
