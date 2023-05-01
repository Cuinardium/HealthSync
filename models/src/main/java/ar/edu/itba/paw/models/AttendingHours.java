package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

  private final List<ThirtyMinuteBlock> attendingBlocksMonday = new ArrayList<>();
  private final List<ThirtyMinuteBlock> attendingBlocksTuesday = new ArrayList<>();
  private final List<ThirtyMinuteBlock> attendingBlocksWednesday = new ArrayList<>();
  private final List<ThirtyMinuteBlock> attendingBlocksThursday = new ArrayList<>();
  private final List<ThirtyMinuteBlock> attendingBlocksFriday = new ArrayList<>();
  private final List<ThirtyMinuteBlock> attendingBlocksSaturday = new ArrayList<>();
  private final List<ThirtyMinuteBlock> attendingBlocksSunday = new ArrayList<>();

  public AttendingHours(
      Collection<ThirtyMinuteBlock> attendingBlocksMonday,
      Collection<ThirtyMinuteBlock> attendingBlocksTuesday,
      Collection<ThirtyMinuteBlock> attendingBlocksWednesday,
      Collection<ThirtyMinuteBlock> attendingBlocksThursday,
      Collection<ThirtyMinuteBlock> attendingBlocksFriday,
      Collection<ThirtyMinuteBlock> attendingBlocksSaturday,
      Collection<ThirtyMinuteBlock> attendingBlocksSunday) {
    this.attendingBlocksMonday.addAll(attendingBlocksMonday);
    this.attendingBlocksTuesday.addAll(attendingBlocksTuesday);
    this.attendingBlocksWednesday.addAll(attendingBlocksWednesday);
    this.attendingBlocksThursday.addAll(attendingBlocksThursday);
    this.attendingBlocksFriday.addAll(attendingBlocksFriday);
    this.attendingBlocksSaturday.addAll(attendingBlocksSaturday);
    this.attendingBlocksSunday.addAll(attendingBlocksSunday);
  }

  // Getters
  public List<ThirtyMinuteBlock> getAttendingBlocksMonday() {
    return attendingBlocksMonday;
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksTuesday() {
    return attendingBlocksTuesday;
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksWednesday() {
    return attendingBlocksWednesday;
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksThursday() {
    return attendingBlocksThursday;
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksFriday() {
    return attendingBlocksFriday;
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksSaturday() {
    return attendingBlocksSaturday;
  }

  public List<ThirtyMinuteBlock> getAttendingBlocksSunday() {
    return attendingBlocksSunday;
  }
}
