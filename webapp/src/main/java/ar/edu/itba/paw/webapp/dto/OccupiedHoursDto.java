package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class OccupiedHoursDto {

  private String date;
  private List<String> hours;

  public static OccupiedHoursDto fromOccupiedHours(LocalDate date, List<ThirtyMinuteBlock> hours) {
    final OccupiedHoursDto dto = new OccupiedHoursDto();

    dto.date = date.toString();
    dto.hours =
        hours.stream().map(ThirtyMinuteBlock::getBlockBeginning).collect(Collectors.toList());

    return dto;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public List<String> getHours() {
    return hours;
  }

  public void setHours(List<String> hours) {
    this.hours = hours;
  }
}
