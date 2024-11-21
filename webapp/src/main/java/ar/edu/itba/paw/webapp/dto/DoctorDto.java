package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.utils.URIUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriInfo;

public class DoctorDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String locale;

  private String address;
  private String city;

  private Float rating;
  private Integer ratingCount;

  private List<LinkDto> links;

  public static DoctorDto fromDoctor(final UriInfo uri, final Doctor doctor) {
    final DoctorDto dto = new DoctorDto();

    dto.id = doctor.getId();
    dto.firstName = doctor.getFirstName();
    dto.lastName = doctor.getLastName();
    dto.email = doctor.getEmail();
    dto.locale = doctor.getLocale().toString();

    dto.address = doctor.getAddress();
    dto.city = doctor.getCity();
    if (doctor.getRatingCount() > 0) {
      dto.rating = doctor.getRating();
      dto.ratingCount = doctor.getRatingCount();
    }

    List<LinkDto> links = new ArrayList<>();

    if (doctor.getImage() != null) {
      URI imageURI = URIUtil.getImageURI(uri, doctor.getImage().getImageId());
      links.add(LinkDto.fromUri(imageURI, "image", HttpMethod.GET));
    }

    doctor
        .getHealthInsurances()
        .forEach(
            hi ->
                links.add(
                    LinkDto.fromUri(
                        URIUtil.getHealthInsuranceURI(uri, hi.ordinal()),
                        "healthinsurance",
                        HttpMethod.GET)));

    URI specialtyURI = URIUtil.getSpecialtyURI(uri, doctor.getSpecialty().ordinal());
    links.add(LinkDto.fromUri(specialtyURI, "specialty", HttpMethod.GET));

    URI reviewsURI = URIUtil.getDoctorReviewURI(uri, doctor.getId());
    links.add(LinkDto.fromUri(reviewsURI, "reviews", HttpMethod.GET));

    URI attendingHoursURI = URIUtil.getAttendingHoursURI(uri, doctor.getId());
    links.add(LinkDto.fromUri(attendingHoursURI, "attendinghours", HttpMethod.GET));

    URI occupiedHoursURI = URIUtil.getOccupiedHoursURI(uri, doctor.getId());
    links.add(LinkDto.fromUri(occupiedHoursURI, "occupiedhours", HttpMethod.GET));

    URI selfURI = URIUtil.getDoctorURI(uri, doctor.getId());
    links.add(LinkDto.fromUri(selfURI, "self", HttpMethod.GET));

    dto.links = links;

    return dto;
  }

  public static List<DoctorDto> fromDoctorList(UriInfo uriInfo, List<Doctor> doctorList) {
    return doctorList.stream().map(d -> fromDoctor(uriInfo, d)).collect(Collectors.toList());
  }

  public void addPrivateLinks(UriInfo uri) {

    URI selfURI = URIUtil.getDoctorURI(uri, id);
    links.add(LinkDto.fromUri(selfURI, "update-self", HttpMethod.PUT));
    links.add(LinkDto.fromUri(selfURI, "update-password", HttpMethod.PATCH));

    URI appointmentsURI = URIUtil.getUserAppointmentURI(uri, id);
    links.add(LinkDto.fromUri(appointmentsURI, "appointments", HttpMethod.GET));
    links.add(
        LinkDto.fromUri(URIUtil.getAppointmentsURI(uri), "create-appointment", HttpMethod.POST));

    URI notificationsURI = URIUtil.getUserNotificationURI(uri, id);
    links.add(LinkDto.fromUri(notificationsURI, "notifications", HttpMethod.GET));

    URI attendingHoursURI = URIUtil.getAttendingHoursURI(uri, id);
    links.add(LinkDto.fromUri(attendingHoursURI, "attendinghours", HttpMethod.GET));
    links.add(LinkDto.fromUri(attendingHoursURI, "update-attendinghours", HttpMethod.PUT));

    URI vacationsURI = URIUtil.getDoctorVacationURI(uri, id);
    links.add(LinkDto.fromUri(vacationsURI, "vacations", HttpMethod.GET));
    links.add(LinkDto.fromUri(vacationsURI, "create-vacation", HttpMethod.POST));
  }

  public void addCreateReviewLink(UriInfo uri) {
    URI createReviewURI = URIUtil.getDoctorReviewURI(uri, id);
    links.add(LinkDto.fromUri(createReviewURI, "create-review", HttpMethod.POST));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
