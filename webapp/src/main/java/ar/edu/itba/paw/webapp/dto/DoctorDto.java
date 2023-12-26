package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Doctor;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.UriInfo;

public class DoctorDto {
  // TODO: add fields
  private String firstName;
  private String lastName;
  private String address;
  private String city;

  private URI healthInsurances;
  private URI attendingHours;
  private URI image;
  private URI self;

  public static DoctorDto fromDoctor(final UriInfo uri, final Doctor doctor) {
    final DoctorDto dto = new DoctorDto();
    // TODO: more fields
    dto.firstName = doctor.getFirstName();
    dto.lastName = doctor.getLastName();
    dto.address = doctor.getAddress();
    dto.city = doctor.getCity();
    // TODO: doctor get Image id para armar el url
    // dto.image =
    // uri.getBaseUriBuilder().path("/images").path(String.valueOf(doctor.getImageId())).build();
    dto.self =
        uri.getBaseUriBuilder().path("/doctors").path(String.valueOf(doctor.getId())).build();
    return dto;
  }

  public static List<DoctorDto> fromDoctorList(UriInfo uriInfo, List<Doctor> doctorList) {
    return doctorList.stream().map(d -> fromDoctor(uriInfo, d)).collect(Collectors.toList());
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public URI getSelf() {
    return self;
  }

  public void setSelf(URI self) {
    this.self = self;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public URI getImage() {
    return image;
  }

  public void setImage(URI image) {
    this.image = image;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
