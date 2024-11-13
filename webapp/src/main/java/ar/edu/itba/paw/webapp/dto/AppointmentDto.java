package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.webapp.utils.URIUtil;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriInfo;

public class AppointmentDto {

  // Properties
  private Long id;
  private LocalDate date;
  private String timeBlock;

  private AppointmentStatus status;
  private String description;

  private String cancelDescription;

  // Links
  private List<LinkDto> links;

  public static AppointmentDto fromAppointment(final UriInfo uri, final Appointment appointment) {
    final AppointmentDto dto = new AppointmentDto();

    // Properties
    dto.id = appointment.getId();
    dto.date = appointment.getDate();
    dto.timeBlock = appointment.getTimeBlock().getBlockBeginning();
    dto.status = appointment.getStatus();
    dto.description = appointment.getDescription();
    dto.cancelDescription = appointment.getCancelDesc();

    // Links
    List<LinkDto> links = new ArrayList<>();
    URI doctorURI = URIUtil.getDoctorURI(uri, appointment.getDoctor().getId());
    links.add(LinkDto.fromUri(doctorURI, "doctor", HttpMethod.GET));

    URI patientURI = URIUtil.getPatientURI(uri, appointment.getPatient().getId());
    links.add(LinkDto.fromUri(patientURI, "patient", HttpMethod.GET));

    URI selfURI = URIUtil.getAppointmentURI(uri, appointment.getId());
    links.add(LinkDto.fromUri(selfURI, "self", HttpMethod.GET));
    links.add(LinkDto.fromUri(selfURI, "update-self", HttpMethod.PATCH));

    URI indicationsURI = URIUtil.getIndicationsURI(uri, appointment.getId());
    links.add(LinkDto.fromUri(indicationsURI, "indications", HttpMethod.GET));
    links.add(LinkDto.fromUri(indicationsURI, "add-indication", HttpMethod.POST));

    dto.links = links;
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

  public String getTimeBlock() {
    return timeBlock;
  }

  public void setTimeBlock(String timeBlock) {
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
  public List<LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<LinkDto> links) {
    this.links = links;
  }
}
