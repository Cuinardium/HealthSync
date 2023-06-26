package ar.edu.itba.paw.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "doctor_vacation")
public class Vacation implements Comparable<Vacation> {

  @EmbeddedId private VacationId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doctor_id")
  @MapsId("doctorId")
  private Doctor doctor;

  protected Vacation() {
    // Just for Hibernate
  }

  public Vacation(
      Long doctorId,
      LocalDate fromDate,
      ThirtyMinuteBlock fromTime,
      LocalDate toDate,
      ThirtyMinuteBlock toTime) {
    this.id = new VacationId(doctorId, fromDate, fromTime, toDate, toTime);
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public VacationId getId() {
    return id;
  }

  public void setId(VacationId id) {
    this.id = id;
  }

  public LocalDate getFromDate() {
    return id.getFromDate();
  }

  public void setFromDate(LocalDate fromDate) {
    id.setFromDate(fromDate);
  }

  public ThirtyMinuteBlock getFromTime() {
    return id.getFromTime();
  }

  public void setFromTime(ThirtyMinuteBlock fromTime) {
    id.setFromTime(fromTime);
  }

  public LocalDate getToDate() {
    return id.getToDate();
  }

  public void setToDate(LocalDate toDate) {
    id.setToDate(toDate);
  }

  public ThirtyMinuteBlock getToTime() {
    return id.getToTime();
  }

  public void setToTime(ThirtyMinuteBlock toTime) {
    id.setToTime(toTime);
  }

  @Override
  public String toString() {
    return "Vacation [" + "id=" + id + ']';
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

    if (!(obj instanceof Vacation)) return false;

    Vacation other = (Vacation) obj;

    return Objects.equals(id, other.id);
  }

  @Override
  public int compareTo(Vacation other) {

    return id.compareTo(other.id);
  }

  public boolean collidesWith(Vacation other) {
    LocalDate vacationFromDate = id.getFromDate();
    ThirtyMinuteBlock vacationFromTime = id.getFromTime();
    LocalDate vacationToDate = id.getToDate();
    ThirtyMinuteBlock vacationToTime = id.getToTime();

    LocalDate otherVacationFromDate = other.id.getFromDate();
    LocalDate otherVacationToDate = other.id.getToDate();
    ThirtyMinuteBlock otherVacationFromTime = other.id.getFromTime();
    ThirtyMinuteBlock otherVacationToTime = other.id.getToTime();

    boolean vacationFromAfterOtherVacationFrom =
        vacationFromDate.isAfter(otherVacationFromDate)
            || (vacationFromDate.equals(otherVacationFromDate)
                && vacationFromTime.compareTo(otherVacationFromTime) >= 0);

    boolean vacationFromBeforeOtherVacationTo =
        vacationFromDate.isBefore(otherVacationToDate)
            || (vacationFromDate.equals(otherVacationToDate)
                && vacationFromTime.compareTo(otherVacationToTime) <= 0);

    boolean vacationToAfterOtherVacationFrom =
        vacationToDate.isAfter(otherVacationFromDate)
            || (vacationToDate.equals(otherVacationFromDate)
                && vacationToTime.compareTo(otherVacationFromTime) >= 0);

    boolean vacationToBeforeOtherVacationTo =
        vacationToDate.isBefore(otherVacationToDate)
            || (vacationToDate.equals(otherVacationToDate)
                && vacationToTime.compareTo(otherVacationToTime) <= 0);

    boolean vacationCollidesWithOtherVacation =
        (vacationFromAfterOtherVacationFrom && vacationFromBeforeOtherVacationTo)
            || (vacationToAfterOtherVacationFrom && vacationToBeforeOtherVacationTo)
            || (vacationFromBeforeOtherVacationTo && vacationToAfterOtherVacationFrom)
            || (vacationFromAfterOtherVacationFrom && vacationToBeforeOtherVacationTo);

    return vacationCollidesWithOtherVacation;
  }

  @Embeddable
  public static class VacationId implements Serializable, Comparable<VacationId> {

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "from_time", nullable = false)
    private ThirtyMinuteBlock fromTime;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "to_time", nullable = false)
    private ThirtyMinuteBlock toTime;

    protected VacationId() {
      // Just for Hibernate
    }

    protected VacationId(
        Long doctorId,
        LocalDate fromDate,
        ThirtyMinuteBlock fromTime,
        LocalDate toDate,
        ThirtyMinuteBlock toTime) {
      this.doctorId = doctorId;
      this.fromDate = fromDate;
      this.fromTime = fromTime;
      this.toDate = toDate;
      this.toTime = toTime;
    }

    public Long getDoctorId() {
      return doctorId;
    }

    public void setDoctorId(Long doctorId) {
      this.doctorId = doctorId;
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

    @Override
    public String toString() {
      return "VacationId ["
          + "doctorId="
          + doctorId
          + ", fromDate="
          + fromDate
          + ", fromTime="
          + fromTime
          + ", toDate="
          + toDate
          + ", toTime="
          + toTime
          + ']';
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;

      result = prime * result + ((doctorId == null) ? 0 : doctorId.hashCode());
      result = prime * result + ((fromDate == null) ? 0 : fromDate.hashCode());
      result = prime * result + ((fromTime == null) ? 0 : fromTime.hashCode());
      result = prime * result + ((toDate == null) ? 0 : toDate.hashCode());
      result = prime * result + ((toTime == null) ? 0 : toTime.hashCode());

      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof VacationId)) return false;

      VacationId other = (VacationId) obj;

      return Objects.equals(this.doctorId, other.doctorId)
          && Objects.equals(this.fromDate, other.fromDate)
          && Objects.equals(this.fromTime, other.fromTime)
          && Objects.equals(this.toDate, other.toDate)
          && Objects.equals(this.toTime, other.toTime);
    }

    @Override
    public int compareTo(VacationId other) {
      int result = this.fromDate.compareTo(other.fromDate);

      if (result == 0) {
        result = this.fromTime.compareTo(other.fromTime);
      }

      if (result == 0) {
        result = this.toDate.compareTo(other.toDate);
      }

      if (result == 0) {
        result = this.toTime.compareTo(other.toTime);
      }

      return result;
    }
  }
}
