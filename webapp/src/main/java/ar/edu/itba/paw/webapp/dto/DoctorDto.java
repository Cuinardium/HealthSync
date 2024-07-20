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
  private String email;

  private URI image;

  private String address;
  private String city;

  private Float rating;
  private Integer ratingCount;

  private List<URI> healthInsurances;
  private URI appointments;
  private URI notifications;

  private URI specialty;
  private URI reviews;
  private URI attendingHours;
  private URI vacations;

  private URI self;

  public static DoctorDto fromDoctor(final UriInfo uri, final Doctor doctor) {
    final DoctorDto dto = new DoctorDto();

    dto.firstName = doctor.getFirstName();
    dto.lastName = doctor.getLastName();
    dto.email = doctor.getEmail();

    if (doctor.getImage() != null) {
      dto.image =
          uri.getBaseUriBuilder()
              .path("/images")
              .path(String.valueOf(doctor.getImage().getImageId()))
              .build();
    }

    dto.address = doctor.getAddress();
    dto.city = doctor.getCity();

    if (doctor.getRatingCount() > 0) {
      dto.rating = doctor.getRating();
      dto.ratingCount = doctor.getRatingCount();
    }

    dto.healthInsurances =
        doctor.getHealthInsurances().stream()
            .map(
                hi ->
                    uri.getBaseUriBuilder()
                        .path("/healthinsurances")
                        .path(String.valueOf(hi.ordinal()))
                        .build())
            .collect(Collectors.toList());

    dto.appointments =
        uri.getBaseUriBuilder().path("/appointments").queryParam("userId", doctor.getId()).build();

    dto.notifications =
        uri.getBaseUriBuilder().path("/notifications").queryParam("userId", doctor.getId()).build();

    dto.specialty =
        uri.getBaseUriBuilder()
            .path("/specialties")
            .path(String.valueOf(doctor.getSpecialty().ordinal()))
            .build();

    dto.reviews =
        uri.getBaseUriBuilder()
            .path("/doctors")
            .path(String.valueOf(doctor.getId()))
            .path("/reviews")
            .build();

    dto.attendingHours =
        uri.getBaseUriBuilder()
            .path("/doctors")
            .path(String.valueOf(doctor.getId()))
            .path("/attendinghours")
            .build();

    dto.vacations =
        uri.getBaseUriBuilder()
            .path("/doctors")
            .path(String.valueOf(doctor.getId()))
            .path("/vacations")
            .build();

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

  public URI getAppointments() {
    return appointments;
  }

  public void setAppointments(URI appointments) {
    this.appointments = appointments;
  }

  public URI getReviews() {
    return reviews;
  }

  public void setReviews(URI reviews) {
    this.reviews = reviews;
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

  public List<URI> getHealthInsurances() {
    return healthInsurances;
  }

  public void setHealthInsurances(List<URI> healthInsurances) {
    this.healthInsurances = healthInsurances;
  }

  public URI getSpecialty() {
    return specialty;
  }

  public void setSpecialty(URI specialty) {
    this.specialty = specialty;
  }

  public URI getAttendingHours() {
    return attendingHours;
  }

  public void setAttendingHours(URI attendingHours) {
    this.attendingHours = attendingHours;
  }

  public URI getVacations() {
    return vacations;
  }

  public void setVacations(URI vacations) {
    this.vacations = vacations;
  }

  public Float getRating() {
    return rating;
  }

  public void setRating(Float rating) {
    this.rating = rating;
  }

  public Integer getRatingCount() {
    return ratingCount;
  }

  public void setRatingCount(Integer ratingCount) {
    this.ratingCount = ratingCount;
  }
}
