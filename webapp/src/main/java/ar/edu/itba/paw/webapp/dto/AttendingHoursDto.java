package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class AttendingHoursDto {

  private String day;
  private List<String> hours;

  public static List<AttendingHoursDto> fromAttendingHours(
      final Set<AttendingHours> attendingHours) {

    // Group by day and map each day to a sorted list of ThirtyMinuteBlock
    Map<DayOfWeek, List<ThirtyMinuteBlock>> attendingHoursMap =
        Arrays.stream(DayOfWeek.values())
            .collect(
                Collectors.toMap(
                    day -> day,
                    day ->
                        attendingHours.stream()
                            .filter(ah -> ah.getDay() == day)
                            .map(AttendingHours::getHourBlock)
                            .sorted()
                            .collect(Collectors.toList())));

    // Convert the map to a sorted list of AttendingHoursDto
    return attendingHoursMap.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(entry -> AttendingHoursDto.fromAttendingHours(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  private static AttendingHoursDto fromAttendingHours(
      final DayOfWeek day, final List<ThirtyMinuteBlock> attendingHours) {
    final AttendingHoursDto dto = new AttendingHoursDto();
    dto.day = day.name();
    dto.hours =
        attendingHours.stream()
            .map(ThirtyMinuteBlock::getBlockBeginning)
            .collect(Collectors.toList());
    return dto;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public List<String> getHours() {
    return hours;
  }

  public void setHours(List<String> hours) {
    this.hours = hours;
  }
}
