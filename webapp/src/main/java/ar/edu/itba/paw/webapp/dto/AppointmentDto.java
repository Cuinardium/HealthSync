package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Appointment;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.UriInfo;

public class AppointmentDto {
  private URI doctor;
  private URI patient;
  private URI self;

  public static AppointmentDto fromAppointment(final UriInfo uri, final Appointment appointment) {
    final AppointmentDto dto = new AppointmentDto();
    dto.doctor =
        uri.getBaseUriBuilder()
            .path("/doctors")
            .path(String.valueOf(appointment.getDoctor().getId()))
            .build();
    dto.patient =
        uri.getBaseUriBuilder()
            .path("/patients")
            .path(String.valueOf(appointment.getPatient().getId()))
            .build();
    dto.self =
        uri.getBaseUriBuilder()
            .path("/appointments")
            .path(String.valueOf(appointment.getId()))
            .build();
    return dto;
  }

  public static List<AppointmentDto> fromAppointmentList(
      final UriInfo uri, final List<Appointment> appointmentList) {
    return appointmentList.stream().map(a -> fromAppointment(uri, a)).collect(Collectors.toList());
  }

  public URI getSelf() {
    return self;
  }

  public void setSelf(URI self) {
    this.self = self;
  }

  public URI getDoctor() {
    return doctor;
  }

  public void setDoctor(URI doctor) {
    this.doctor = doctor;
  }

  public URI getPatient() {
    return patient;
  }

  public void setPatient(URI patient) {
    this.patient = patient;
  }
}
