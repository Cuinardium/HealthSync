import api from "./api";
import {Appointment} from "../models/Appointment";

const AppointmentsEndpoint = '/appointments'

//TODO revisar tema headers
export async function getAppointments(id: string): Promise<Appointment[]>{
    const response = await api.get(AppointmentsEndpoint,{
        params:{
            userId: id
        }
    });

    const responseList = [];

    for (const appointment of response.data){
        responseList.push(Appointment.fromJson(appointment))
    }

    return responseList;
}

export async function getAppointmentsById(appId: string): Promise<Appointment>{
    const response = await api.get(`${AppointmentsEndpoint}/${appId}`);

    return Appointment.fromJson(response.data);
}

