package ar.edu.itba.paw.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendingHours {

  // Default attending hours, monday to friday, 8am to 6pm
  public static final AttendingHours DEFAULT_ATTENDING_HOURS =
      new AttendingHours(
          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
          new ArrayList<>(),
          new ArrayList<>());

  private final Map<DayOfWeek, List<ThirtyMinuteBlock>> attendingHours = new HashMap<>();

  // From collection of 30 minute blocks
  public AttendingHours(
      Collection<ThirtyMinuteBlock> attendingBlocksMonday,
      Collection<ThirtyMinuteBlock> attendingBlocksTuesday,
      Collection<ThirtyMinuteBlock> attendingBlocksWednesday,
      Collection<ThirtyMinuteBlock> attendingBlocksThursday,
      Collection<ThirtyMinuteBlock> attendingBlocksFriday,
      Collection<ThirtyMinuteBlock> attendingBlocksSaturday,
      Collection<ThirtyMinuteBlock> attendingBlocksSunday) {

    this.attendingHours.put(DayOfWeek.MONDAY, new ArrayList<>(attendingBlocksMonday));
    this.attendingHours.put(DayOfWeek.TUESDAY, new ArrayList<>(attendingBlocksTuesday));
    this.attendingHours.put(DayOfWeek.WEDNESDAY, new ArrayList<>(attendingBlocksWednesday));
    this.attendingHours.put(DayOfWeek.THURSDAY, new ArrayList<>(attendingBlocksThursday));
    this.attendingHours.put(DayOfWeek.FRIDAY, new ArrayList<>(attendingBlocksFriday));
    this.attendingHours.put(DayOfWeek.SATURDAY, new ArrayList<>(attendingBlocksSaturday));
    this.attendingHours.put(DayOfWeek.SUNDAY, new ArrayList<>(attendingBlocksSunday));
  }

  // Getter for lists of 30 minute blocks
  public List<ThirtyMinuteBlock> getAttendingBlocksForDay(DayOfWeek day) {
    return attendingHours.get(day);
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksForDate(LocalDate date) {
    return attendingHours.get(date.getDayOfWeek());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof AttendingHours)) return false;
    AttendingHours other = (AttendingHours) obj;
    return this.attendingHours.equals(other.attendingHours);
  }
}
