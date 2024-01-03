package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Patient;
import java.net.URI;
import javax.ws.rs.core.UriInfo;

public class PatientDto {

  private String firstName;
  private String lastName;
  private URI healthInsurance;
  private URI self;

  public static PatientDto fromPatient(UriInfo uri, Patient patient) {
    PatientDto dto = new PatientDto();

    dto.firstName = patient.getFirstName();
    dto.lastName = patient.getLastName();
    dto.healthInsurance =
        uri.getBaseUriBuilder()
            .path("/healthinsurances")
            .path(String.valueOf(patient.getHealthInsurance().ordinal()))
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
}
