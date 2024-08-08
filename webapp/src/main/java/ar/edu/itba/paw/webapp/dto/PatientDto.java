package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Patient;
import java.net.URI;
import javax.ws.rs.core.UriInfo;

public class PatientDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;

  private URI image;

  private URI healthInsurance;
  private URI appointments;
  private URI notifications;

  private URI self;

  public static PatientDto fromPatient(UriInfo uri, Patient patient) {
    PatientDto dto = new PatientDto();

    dto.id = patient.getId();
    dto.firstName = patient.getFirstName();
    dto.lastName = patient.getLastName();
    dto.email = patient.getEmail();

    dto.image =
        uri.getBaseUriBuilder()
            .path("/images")
            .path(String.valueOf(patient.getImage().getImageId()))
            .build();

    dto.healthInsurance =
        uri.getBaseUriBuilder()
            .path("/healthinsurances")
            .path(String.valueOf(patient.getHealthInsurance().ordinal()))
            .build();

    dto.appointments =
        uri.getBaseUriBuilder().path("/appointments").queryParam("userId", patient.getId()).build();

    dto.notifications =
        uri.getBaseUriBuilder()
            .path("/notifications")
            .queryParam("userId", patient.getId())
            .build();

    dto.self =
        uri.getBaseUriBuilder().path("/patients").path(String.valueOf(patient.getId())).build();

    return dto;
  }

  public URI getSelf() {
    return self;
  }

  public void setSelf(URI self) {
    this.self = self;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public URI getHealthInsurance() {
    return healthInsurance;
  }

  public void setHealthInsurance(URI healthInsurance) {
    this.healthInsurance = healthInsurance;
  }

  public URI getAppointments() {
    return appointments;
  }

  public void setAppointments(URI appointments) {
    this.appointments = appointments;
  }

  public URI getNotifications() {
    return notifications;
  }

  public void setNotifications(URI notifications) {
    this.notifications = notifications;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public URI getImage() {
    return image;
  }

  public void setImage(URI image) {
    this.image = image;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
