package ar.edu.itba.paw.models;

import java.time.LocalDate;

public class Appointment {

  @Override
  public String toString() {
    return "Appointment [id="
        + id
        + ", date="
        + date
        + ", timeBlock="
        + timeBlock
        + ", status="
        + status
        + ", description="
        + description
        + ", cancelDesc="
        + cancelDesc
        + " "
        + patient
        + " "
        + doctor
        + "]";
  }

  private final long id;
  private final Patient patient;
  private final Doctor doctor;

  private final LocalDate date;
  private final ThirtyMinuteBlock timeBlock;

  private final AppointmentStatus status;
  private final String description;

  private final String cancelDesc;

  public Appointment(
      long id,
      Patient patient,
      Doctor doctor,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      AppointmentStatus status,
      String description,
      String cancelDesc) {
    this.id = id;
    this.patient = patient;
    this.doctor = doctor;
    this.date = date;
    this.timeBlock = timeBlock;
    this.status = status;
    this.description = description;
    this.cancelDesc = cancelDesc;
  }

  public long getId() {
    return id;
  }

  public long getPatientId() {
    return patient.getId();
  }

  public Patient getPatient() {
    return patient;
  }

  public long getDoctorId() {
    return doctor.getId();
  }

  public Doctor getDoctor() {
    return doctor;
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

  public String getCancelDesc() {
    return cancelDesc;
  }
}
