package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.webapp.utils.URIUtil;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.UriInfo;

public class ReviewDto {

  // Properties
  private Long id;
  private LocalDate date;
  private String description;
  private Short rating;

  // Links
  private List<LinkDto> links;

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
    List<LinkDto> links = new ArrayList<>(2);

    URI patientURI = URIUtil.getPatientURI(uri, patientId);
    links.add(LinkDto.fromUri(patientURI, "patient", "GET"));

    URI selfURI = URIUtil.getReviewURI(uri, doctorId, dto.id);
    links.add(LinkDto.fromUri(selfURI, "self", "GET"));

    dto.links = links;

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

  public Short getRating() {
    return rating;
  }

  public void setRating(Short rating) {
    this.rating = rating;
  }

  public List<LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<LinkDto> links) {
    this.links = links;
  }
}
