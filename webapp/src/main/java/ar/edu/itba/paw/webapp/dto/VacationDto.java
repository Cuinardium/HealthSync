package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.models.Vacation;
import java.net.URI;
import java.time.LocalDate;
import javax.ws.rs.core.UriInfo;

public class VacationDto {

  private long id;
  private LocalDate fromDate;
  private ThirtyMinuteBlock fromTime;
  private LocalDate toDate;
  private ThirtyMinuteBlock toTime;

  private URI doctor;
  private URI self;

  public static VacationDto fromVacation(UriInfo uri, Vacation vacation) {

    final VacationDto dto = new VacationDto();

    final long doctorId = vacation.getDoctor().getId();

    dto.id = vacation.getId();
    dto.fromDate = vacation.getFromDate();
    dto.toDate = vacation.getToDate();
    dto.fromTime = vacation.getFromTime();
    dto.toTime = vacation.getToTime();

    dto.doctor = uri.getBaseUriBuilder().path("doctors").path(String.valueOf(doctorId)).build();
    dto.self =
        uri.getBaseUriBuilder()
            .path("doctors")
            .path(String.valueOf(doctorId))
            .path("vacations")
            .path(String.valueOf(dto.id))
            .build();

    return dto;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public LocalDate getFromDate() {
    return fromDate;
  }

  public void setFromDate(LocalDate fromDate) {
    this.fromDate = fromDate;
  }

  public ThirtyMinuteBlock getFromTime() {
    return fromTime;
  }

  public void setFromTime(ThirtyMinuteBlock fromTime) {
    this.fromTime = fromTime;
  }

  public LocalDate getToDate() {
    return toDate;
  }

  public void setToDate(LocalDate toDate) {
    this.toDate = toDate;
  }

  public ThirtyMinuteBlock getToTime() {
    return toTime;
  }

  public void setToTime(ThirtyMinuteBlock toTime) {
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
