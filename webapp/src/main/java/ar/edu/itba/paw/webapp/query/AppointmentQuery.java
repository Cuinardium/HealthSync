package ar.edu.itba.paw.webapp.query;

import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.webapp.annotations.ExistsInEnumString;

import javax.ws.rs.QueryParam;

public class AppointmentQuery {

    @QueryParam("status")
    @ExistsInEnumString(enumClass = AppointmentStatus.class, message = "ExistsInEnumString.AppointmentQuery.status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppointmentStatus getAppointmentStatus() {
        if(status == null) {
            return null;
        }
        return AppointmentStatus.valueOf(status);
    }
}
