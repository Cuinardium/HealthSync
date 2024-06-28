package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Review;
import java.net.URI;
import java.time.LocalDate;
import javax.ws.rs.core.UriInfo;

public class ReviewDto {

  // Properties
  private Long id;
  private LocalDate date;
  private String description;
  private Short rating;

  // Links
  private URI doctor;
  private URI patient;
  private URI self;

  public static ReviewDto fromReview(final UriInfo uri, final Review review) {
    final ReviewDto dto = new ReviewDto();

    // Properties
    dto.id = review.getId();
    dto.date = review.getDate();
    dto.description = review.getDescription();
    dto.rating = review.getRating();

    Long doctorId = review.getDoctor().getId();
    Long patientId = review.getPatient().getId();

    // Links
    dto.doctor = uri.getBaseUriBuilder().path("/doctors").path(String.valueOf(doctorId)).build();
    dto.patient = uri.getBaseUriBuilder().path("/patients").path(String.valueOf(patientId)).build();
    dto.self =
        uri.getBaseUriBuilder().path("/doctors").path(String.valueOf(doctorId)).path("/reviews")
            .path(String.valueOf(dto.id)).build();

    return dto;
  }

  // Getters

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

  public Short getRating() {
    return rating;
  }

  // Setters

  public void setRating(Short rating) {
    this.rating = rating;
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

  public URI getSelf() {
    return self;
  }

  public void setSelf(URI self) {
    this.self = self;
  }
}
