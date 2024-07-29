package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.models.Vacation;
import java.net.URI;
import java.time.LocalDate;
import javax.ws.rs.core.UriInfo;

public class VacationDto {

  private LocalDate fromDate;
  private String fromTime;
  private LocalDate toDate;
  private String toTime;

  private URI doctor;
  private URI self;

  public static VacationDto fromVacation(UriInfo uri, Vacation vacation) {

    final VacationDto dto = new VacationDto();

    final long doctorId = vacation.getDoctor().getId();

    dto.fromDate = vacation.getFromDate();
    dto.toDate = vacation.getToDate();
    dto.fromTime = vacation.getFromTime().getBlockBeginning();
    dto.toTime = vacation.getToTime().getBlockEnd();

    dto.doctor = uri.getBaseUriBuilder().path("doctors").path(String.valueOf(doctorId)).build();
    dto.self =
        uri.getBaseUriBuilder()
            .path("doctors")
            .path(String.valueOf(doctorId))
            .path("vacations")
            .path(String.valueOf(vacation.getId()))
            .build();

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

  public void setFromTime(String  fromTime) {
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

  public URI getDoctor() {
    return doctor;
  }

  public void setDoctor(URI doctor) {
    this.doctor = doctor;
  }

  public URI getSelf() {
    return self;
  }

  public void setSelf(URI self) {
    this.self = self;
  }
}
