package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Indication;
import java.net.URI;
import java.time.LocalDate;
import javax.ws.rs.core.UriInfo;

public class IndicationDto {

  // Properties
  private Long id;
  private LocalDate date;
  private String description;

  // Links
  private URI appointment;
  private URI creator;
  private URI file;
  private URI self;

  public static IndicationDto fromIndication(final UriInfo uri, final Indication indication) {
    final IndicationDto dto = new IndicationDto();

    // Properties
    dto.id = indication.getId();
    dto.date = indication.getDate();
    dto.description = indication.getDescription();

    long appointmentId = indication.getAppointment().getId();
    long userId = indication.getUser().getId();
    boolean isDoctor = indication.getAppointment().getDoctor().getId() == userId;
    long indicationId = indication.getId();
    long fileId = indication.getFile() != null ? indication.getFile().getFileId() : -1;

    // Links
    dto.appointment =
        uri.getBaseUriBuilder().path("appointments").path(String.valueOf(appointmentId)).build();
    dto.creator =
        uri.getBaseUriBuilder()
            .path(isDoctor ? "doctors" : "patients")
            .path(String.valueOf(userId))
            .build();
    dto.file =
        fileId != -1
            ? uri.getBaseUriBuilder().path("files").path(String.valueOf(fileId)).build()
            : null;
    dto.self =
        uri.getBaseUriBuilder()
            .path("appointments")
            .path(String.valueOf(appointmentId))
            .path("indications")
            .path(String.valueOf(indicationId))
            .build();

    return dto;
  }

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public URI getAppointment() {
    return appointment;
  }

  public void setAppointment(URI appointment) {
    this.appointment = appointment;
  }

  public URI getCreator() {
    return creator;
  }

  public void setCreator(URI creator) {
    this.creator = creator;
  }

  public URI getFile() {
    return file;
  }

  public void setFile(URI file) {
    this.file = file;
  }

  public URI getSelf() {
    return self;
  }

  public void setSelf(URI self) {
    this.self = self;
  }
}
