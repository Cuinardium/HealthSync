package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.models.Vacation;
import ar.edu.itba.paw.webapp.utils.URIUtil;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import javax.ws.rs.core.UriInfo;

public class VacationDto {

  private Long id;
  private LocalDate fromDate;
  private String fromTime;
  private LocalDate toDate;
  private String toTime;

  private List<LinkDto> links;

  public static VacationDto fromVacation(UriInfo uri, Vacation vacation) {

    final VacationDto dto = new VacationDto();

    final long doctorId = vacation.getDoctor().getId();

    dto.id = vacation.getId();
    dto.fromDate = vacation.getFromDate();
    dto.fromTime = vacation.getFromTime().getBlockBeginning();
    dto.toTime = vacation.getToTime().getBlockEnd();

    dto.toDate =
        vacation.getToTime() == ThirtyMinuteBlock.BLOCK_23_30
            ? vacation.getToDate().plusDays(1)
            : vacation.getToDate();

    List<LinkDto> links = new java.util.ArrayList<>(2);

    URI selfURI = URIUtil.getVacationURI(uri, doctorId, vacation.getId());
    links.add(LinkDto.fromUri(selfURI, "self", "GET"));
    links.add(LinkDto.fromUri(selfURI, "delete-self", "DELETE"));

    dto.links = links;

    return dto;
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
  }

  public String getFromTime() {
    return fromTime;
  }

  public void setFromTime(String fromTime) {
    this.fromTime = fromTime;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public void setToDate(LocalDate toDate) {
    this.toDate = toDate;
  }

  public String getToTime() {
    return toTime;
  }

  public void setToTime(String toTime) {
    this.toTime = toTime;
  }

  public List<LinkDto> getLinks() {
    return links;
  }

  public void setLinks(List<LinkDto> links) {
    this.links = links;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
