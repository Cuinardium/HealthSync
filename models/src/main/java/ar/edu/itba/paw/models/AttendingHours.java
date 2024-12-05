package ar.edu.itba.paw.models;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "doctor_attending_hours")
public class AttendingHours {
  @EmbeddedId private AttendingHoursId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doctor_id")
  @MapsId("doctorId")
  private Doctor doctor;

  protected AttendingHours() {
    // Solo para hibernate
  }

  public AttendingHours(Long doctorId, DayOfWeek day, ThirtyMinuteBlock hourBlock) {
    this.id = new AttendingHoursId(doctorId, day, hourBlock);
  }

  public static List<AttendingHours> createFromList(
      Long doctorId, DayOfWeek day, Collection<ThirtyMinuteBlock> blocks) {
    List<AttendingHours> attendingHours = new ArrayList<>();
    for (ThirtyMinuteBlock block : blocks) {
      attendingHours.add(new AttendingHours(doctorId, day, block));
    }
    return attendingHours;
  }

  // From collection of 30 minute blocks

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public AttendingHoursId getId() {
    return id;
  }

  public DayOfWeek getDay() {
    return id.day;
  }

  public ThirtyMinuteBlock getHourBlock() {
    return id.hourBlock;
  }

  public void setId(AttendingHoursId id) {
    this.id = id;
  }

  // Getter for lists of 30 minute blocks

  @Override
  public String toString() {
    return "AttendingHours [id=" + id + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof AttendingHours)) return false;
    AttendingHours other = (AttendingHours) obj;
    return Objects.equals(this.id, other.id);
  }

  @Embeddable
  public static class AttendingHoursId implements Serializable {
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "day", nullable = false)
    private DayOfWeek day;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "hour_block")
    private ThirtyMinuteBlock hourBlock;

    protected AttendingHoursId() {
      // Solo para hibernate
    }

    protected AttendingHoursId(Long doctor_id, DayOfWeek day, ThirtyMinuteBlock hourBlock) {
      this.doctorId = doctor_id;
      this.day = day;
      this.hourBlock = hourBlock;
    }

    public Long getDoctorId() {
      return doctorId;
    }

    public void setDoctorId(Long doctorId) {
      this.doctorId = doctorId;
    }

    public DayOfWeek getDay() {
      return day;
    }

    public void setDay(DayOfWeek day) {
      this.day = day;
    }

    @Override
    public String toString() {
      return "AttendingHoursId [doctorId="
          + doctorId
          + ", day="
          + day
          + ", hourBlock="
          + hourBlock
          + "]";
    }

    public ThirtyMinuteBlock getHourBlock() {
      return hourBlock;
    }

    public void setHourBlock(ThirtyMinuteBlock hourBlock) {
      this.hourBlock = hourBlock;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      // result = prime * result + ((doctorId == null) ? 0 : doctorId.hashCode());
      result = prime * result + ((day == null) ? 0 : day.hashCode());
      result = prime * result + ((hourBlock == null) ? 0 : hourBlock.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof AttendingHoursId)) return false;
      AttendingHoursId other = (AttendingHoursId) obj;
      return Objects.equals(this.doctorId, other.doctorId)
          && Objects.equals(this.day, other.day)
          && Objects.equals(this.hourBlock, other.hourBlock);
    }
  }
}
