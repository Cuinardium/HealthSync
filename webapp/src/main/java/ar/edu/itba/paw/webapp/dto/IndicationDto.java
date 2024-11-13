package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Indication;
import ar.edu.itba.paw.webapp.utils.URIUtil;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.UriInfo;

public class IndicationDto {

  // Properties
  private Long id;
  private LocalDate date;
  private String description;

  // Links
  private List<LinkDto> links;

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
    List<LinkDto> links = new ArrayList<>(3);
    URI creatorURI =
        isDoctor ? URIUtil.getDoctorURI(uri, userId) : URIUtil.getPatientURI(uri, userId);
    links.add(LinkDto.fromUri(creatorURI, "creator", HttpMethod.GET));

    if (fileId != -1) {
      URI fileURI = URIUtil.getFileURI(uri, appointmentId, fileId);
      links.add(LinkDto.fromUri(fileURI, "file", HttpMethod.GET));
    }

    URI self = URIUtil.getIndicationURI(uri, appointmentId, indicationId);
    links.add(LinkDto.fromUri(self, "self", HttpMethod.GET));

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

  public List<LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<LinkDto> links) {
    this.links = links;
  }
}
