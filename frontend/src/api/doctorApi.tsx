import api from "./api";
import {Doctor} from "../models/Doctor";

const doctorEndpoint = '/doctors'


//TODO revisar tema headers
export async function createDoctor(doctor: Doctor): Promise<Doctor>{
    const response = await api.post(doctorEndpoint, Doctor.toJson(doctor));
    return response.data;
}

export async function getDoctorById(id : String): Promise<Doctor>{
    const response = await api.get(`${doctorEndpoint}/${id}`);
    return Doctor.fromJson(response.data)
}

