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

  // From bits that encode 30 minute blocks
  public AttendingHours(
      long attendingBlocksMonday,
      long attendingBlocksTuesday,
      long attendingBlocksWednesday,
      long attendingBlocksThursday,
      long attendingBlocksFriday,
      long attendingBlocksSaturday,
      long attendingBlocksSunday) {

    this.attendingHours.put(
        DayOfWeek.MONDAY, new ArrayList<>(attendingHoursFromBits(attendingBlocksMonday)));
    this.attendingHours.put(
        DayOfWeek.TUESDAY, new ArrayList<>(attendingHoursFromBits(attendingBlocksTuesday)));
    this.attendingHours.put(
        DayOfWeek.WEDNESDAY, new ArrayList<>(attendingHoursFromBits(attendingBlocksWednesday)));
    this.attendingHours.put(
        DayOfWeek.THURSDAY, new ArrayList<>(attendingHoursFromBits(attendingBlocksThursday)));
    this.attendingHours.put(
        DayOfWeek.FRIDAY, new ArrayList<>(attendingHoursFromBits(attendingBlocksFriday)));
    this.attendingHours.put(
        DayOfWeek.SATURDAY, new ArrayList<>(attendingHoursFromBits(attendingBlocksSaturday)));
    this.attendingHours.put(
        DayOfWeek.SUNDAY, new ArrayList<>(attendingHoursFromBits(attendingBlocksSunday)));
  }

  // Getter for lists of 30 minute blocks
  public List<ThirtyMinuteBlock> getAttendingBlocksForDay(DayOfWeek day) {
    return attendingHours.get(day);
  }
  
  // Getter for bits that encode 30 minute blocks
  public long getAttendingBlocksForDayAsBits(DayOfWeek day) {
    return attendingHoursToBits(attendingHours.get(day));
  }

  
  public List<ThirtyMinuteBlock> getAttendingBlocksForDate(LocalDate date) {
    return attendingHours.get(date.getDayOfWeek());
  }

  public long attendingHoursToBits(Collection<ThirtyMinuteBlock> attendingHours) {

    long bits = 0;

    for (ThirtyMinuteBlock block : attendingHours) {
      bits |= (1L << block.ordinal());
    }

    return bits;
  }

  // Most significant 16 bits dont encode anything
  // The 48 least significant bits encode 30 minute blocks
  // If the bit is 1 the doctor is available, otherwise it is not
  // The least significant bit encodes the (0:00, 0:30) block
  // The 48th bit encodes the (23:3, 0:00) block
  private Collection<ThirtyMinuteBlock> attendingHoursFromBits(long bits) {

    Collection<ThirtyMinuteBlock> blocks = new ArrayList<>();
    ThirtyMinuteBlock[] values = ThirtyMinuteBlock.values();

    for (int i = 0; i < 48; i++) {
      if ((bits & (1L << i)) != 0) {
        blocks.add(values[i]);
      }
    }

    return blocks;
  }
}
