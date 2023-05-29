package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "doctor_attending_hours")
public class AttendingHours {
  @EmbeddedId
  private AttendingHoursId id;

  @ManyToOne
  @JoinColumn(name = "doctor_id", nullable = false)
  private Doctor doctor;


  // Default attending hours, monday to friday, 8am to 6pm
//  public static final AttendingHours DEFAULT_ATTENDING_HOURS =
//      new AttendingHours(
//          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
//          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
//          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
//          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
//          ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_08_00, ThirtyMinuteBlock.BLOCK_17_30),
//          new ArrayList<>(),
//          new ArrayList<>());



  AttendingHours() {
    // Solo para hibernate
  }

  public AttendingHours(
          Collection<ThirtyMinuteBlock> attendingBlocksMonday,
          Collection<ThirtyMinuteBlock> attendingBlocksTuesday,
          Collection<ThirtyMinuteBlock> attendingBlocksWednesday,
          Collection<ThirtyMinuteBlock> attendingBlocksThursday,
          Collection<ThirtyMinuteBlock> attendingBlocksFriday,
          Collection<ThirtyMinuteBlock> attendingBlocksSaturday,
          Collection<ThirtyMinuteBlock> attendingBlocksSunday) {

  }
  // From collection of 30 minute blocks

  public AttendingHoursId getId() {
    return id;
  }

  public ThirtyMinuteBlock getHourBlock() {
    return id.hourBlock;
  }

  public void setId(AttendingHoursId id) {
    this.id = id;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  // Getter for lists of 30 minute blocks


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AttendingHours that = (AttendingHours) o;
    return Objects.equals(id, that.id) && Objects.equals(doctor, that.doctor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, doctor);
  }

  @Embeddable
  public class AttendingHoursId implements Serializable {

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "day", nullable = false)
    private DayOfWeek day;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "hour_block")
    private ThirtyMinuteBlock hourBlock;

    AttendingHoursId() {
      // Solo para hibernate
    }

    public DayOfWeek getDay() {
      return day;
    }

    public void setDay(DayOfWeek day) {
      this.day = day;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      AttendingHoursId that = (AttendingHoursId) o;
      return day == that.day && hourBlock == that.hourBlock;
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, day, hourBlock);
    }
  }
}
