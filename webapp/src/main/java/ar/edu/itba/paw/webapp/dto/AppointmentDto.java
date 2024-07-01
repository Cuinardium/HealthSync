package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.UriInfo;

public class AppointmentDto {

  // Properties
  private Long id;
  private LocalDate date;
  private ThirtyMinuteBlock timeBlock;

  private AppointmentStatus status;
  private String description;

  private String cancelDescription;

  // Links
  private URI doctor;
  private URI patient;
  private URI self;

  public static AppointmentDto fromAppointment(final UriInfo uri, final Appointment appointment) {
    final AppointmentDto dto = new AppointmentDto();

    // Properties
    dto.id = appointment.getId();
    dto.date = appointment.getDate();
    dto.timeBlock = appointment.getTimeBlock();
    dto.status = appointment.getStatus();
    dto.description = appointment.getDescription();
    dto.cancelDescription = appointment.getCancelDesc();

    // Links

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

  // Properties

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public ThirtyMinuteBlock getTimeBlock() {
    return timeBlock;
  }

  public void setTimeBlock(ThirtyMinuteBlock timeBlock) {
    this.timeBlock = timeBlock;
  }

  public AppointmentStatus getStatus() {
    return status;
  }

  public void setStatus(AppointmentStatus status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCancelDescription() {
    return cancelDescription;
  }

  public void setCancelDescription(String cancelDescription) {
    this.cancelDescription = cancelDescription;
  }

  // Links

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
