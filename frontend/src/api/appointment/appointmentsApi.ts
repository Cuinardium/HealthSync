import {axios} from "../axios";
import {Appointment} from "./Appointment";

const AppointmentsEndpoint = '/appointments'

//TODO revisar tema headers
export async function getAppointments(id: string): Promise<Appointment[]>{
    const response = await axios.get(AppointmentsEndpoint,{
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
    const response = await axios.get(`${AppointmentsEndpoint}/${appId}`);

    return Appointment.fromJson(response.data);
}

