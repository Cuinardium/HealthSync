package ar.edu.itba.paw.models;

import java.time.LocalDate;

public class Appointment {

  @Override
  public String toString() {
    return "Appointment [id="
        + id
        + ", patientId="
        + patientId
        + ", doctorId="
        + doctorId
        + ", date="
        + date
        + ", timeBlock="
        + timeBlock
        + ", status="
        + status
        + ", description="
        + description
        + "]";
  }

  private final long id;
  private final long patientId;
  private final long doctorId;

  private final LocalDate date;
  private final ThirtyMinuteBlock timeBlock;

  private final AppointmentStatus status;
  private final String description;

  public Appointment(
      long id,
      long patientId,
      long doctorId,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      AppointmentStatus status,
      String description) {
    this.id = id;
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.date = date;
    this.timeBlock = timeBlock;
    this.status = status;
    this.description = description;
  }

  public long getId() {
    return id;
  }

  public long getPatientId() {
    return patientId;
  }

  public long getDoctorId() {
    return doctorId;
  }

  public LocalDate getDate() {
    return date;
  }

  public ThirtyMinuteBlock getTimeBlock() {
    return timeBlock;
  }

  public AppointmentStatus getStatus() {
    return status;
  }

  public String getDescription() {
    return description;
  }
}
