package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.utils.URIUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.UriInfo;

public class PatientDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String locale;

  private List<LinkDto> links;

  public static PatientDto fromPatient(UriInfo uri, Patient patient) {
    PatientDto dto = new PatientDto();

    dto.id = patient.getId();
    dto.firstName = patient.getFirstName();
    dto.lastName = patient.getLastName();
    dto.email = patient.getEmail();
    dto.locale = patient.getLocale().toString();

    List<LinkDto> links = new ArrayList<>();

    if (patient.getImage() != null) {
      URI image = URIUtil.getImageURI(uri, patient.getImage().getImageId());
      links.add(LinkDto.fromUri(image, "image", "GET"));
    }

    URI healthInsurance = URIUtil.getHealthInsuranceURI(uri, patient.getHealthInsurance().ordinal());
    links.add(LinkDto.fromUri(healthInsurance, "health-insurance", "GET"));

    URI self = URIUtil.getPatientURI(uri, patient.getId());
    links.add(LinkDto.fromUri(self, "self", "GET"));

    dto.links = links;

    return dto;
  }

  public void addPrivateLinks(UriInfo uri) {

    URI self = URIUtil.getPatientURI(uri, id);
    links.add(LinkDto.fromUri(self, "update-self", "PUT"));

    URI appointments = URIUtil.getUserAppointmentURI(uri, id);
    links.add(LinkDto.fromUri(appointments, "appointments", "GET"));

    URI notifications = URIUtil.getUserNotificationURI(uri, id);
    links.add(LinkDto.fromUri(notifications, "notifications", "GET"));

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public List<LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<LinkDto> links) {
    this.links = links;
  }
}
