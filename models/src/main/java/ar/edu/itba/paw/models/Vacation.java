package ar.edu.itba.paw.models;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "doctor_vacation")
public class Vacation implements Comparable<Vacation> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vacation_id_seq")
  @SequenceGenerator(sequenceName = "vacation_id_seq", name = "vacation_id_seq", allocationSize = 1)
  @Column(name = "vacation_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doctor_id", nullable = false)
  private Doctor doctor;

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

  public static Builder builder() {
    return new Builder();
  }

  protected Vacation() {
    // Just for Hibernate
  }

  private Vacation(Builder builder) {
    this.id = builder.id;
    this.doctor = builder.doctor;
    this.fromDate = builder.fromDate;
    this.toDate = builder.toDate;
    this.fromTime = builder.fromTime;
    this.toTime = builder.toTime;
  }


  public Long getId() {
    return id;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
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
    return "Vacation ["
        + "id="
        + id
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
    return Objects.hash(id);
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

  public boolean collidesWith(Vacation other) {
    boolean vacationFromAfterOtherVacationFrom =
        fromDate.isAfter(other.fromDate)
            || (fromDate.equals(other.fromDate) && fromTime.compareTo(other.fromTime) >= 0);

    boolean vacationFromBeforeOtherVacationTo =
        fromDate.isBefore(other.toDate)
            || (fromDate.equals(other.toDate) && fromTime.compareTo(other.toTime) <= 0);

    boolean vacationToAfterOtherVacationFrom =
        toDate.isAfter(other.fromDate)
            || (toDate.equals(other.fromDate) && toTime.compareTo(other.fromTime) >= 0);

    boolean vacationToBeforeOtherVacationTo =
        toDate.isBefore(other.toDate)
            || (toDate.equals(other.toDate) && toTime.compareTo(other.toTime) <= 0);

    boolean vacationCollidesWithOtherVacation =
        (vacationFromAfterOtherVacationFrom && vacationFromBeforeOtherVacationTo)
            || (vacationToAfterOtherVacationFrom && vacationToBeforeOtherVacationTo)
            || (fromDate.isBefore(other.toDate) && toDate.isAfter(other.fromDate))
            || (fromDate.isAfter(other.fromDate) && toDate.isBefore(other.toDate));

    return vacationCollidesWithOtherVacation;
  }

  public static class Builder {

    private Long id;
    private Doctor doctor;
    private LocalDate fromDate;
    private ThirtyMinuteBlock fromTime;
    private LocalDate toDate;
    private ThirtyMinuteBlock toTime;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder doctor(Doctor doctor) {
      this.doctor = doctor;
      return this;
    }
    public Builder fromDate(LocalDate fromDate) {
      this.fromDate = fromDate;
      return this;
    }

    public Builder fromTime(ThirtyMinuteBlock fromTime) {
      this.fromTime = fromTime;
      return this;
    }

    public Builder toDate(LocalDate toDate) {
      this.toDate = toDate;
      return this;
    }

    public Builder toTime(ThirtyMinuteBlock toTime) {
      this.toTime = toTime;
      return this;
    }

    public Vacation build() {
      return new Vacation(this);
    }
  }
}
