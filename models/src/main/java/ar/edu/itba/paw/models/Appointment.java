package ar.edu.itba.paw.models;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_appointment_id_seq")
  @SequenceGenerator(
    sequenceName = "appointment_appointment_id_seq",
    name = "appointment_appointment_id_seq",
    allocationSize = 1
  )
  @Column(name = "appointment_id")
  private Long id;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "patient_id")
  private Patient patient;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "doctor_id")
  private Doctor doctor;

  @Column(name = "appointment_date", nullable = false)
  private LocalDate date;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "appointment_time", nullable = false)
  private ThirtyMinuteBlock timeBlock;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "status_code", nullable = false)
  private AppointmentStatus status;

  @Column(name = "appointment_description", length = 1000)
  private String description;

  @Column(name = "cancel_description", length = 1000)
  private String cancelDesc;

  @Column(name = "indications", length = 1000)
  private String indications;

  public String getIndications() {
    return indications;
  }

  public void setIndications(String indications) {
    this.indications = indications;
  }

  /* package */ Appointment() {
    // Solo para hibernate
  }

  public Appointment(
      Long id,
      Patient patient,
      Doctor doctor,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      AppointmentStatus status,
      String description,
      String cancelDesc,
      String indications) {
    this.id = id;
    this.patient = patient;
    this.doctor = doctor;
    this.date = date;
    this.timeBlock = timeBlock;
    this.status = status;
    this.description = description;
    this.cancelDesc = cancelDesc;
    this.indications = indications;
  }

  public Appointment(Builder builder) {
    this.id = builder.id;
    this.patient = builder.patient;
    this.doctor = builder.doctor;
    this.date = builder.date;
    this.timeBlock = builder.timeBlock;
    this.status = builder.status;
    this.description = builder.description;
    this.cancelDesc = builder.cancelDescription;
    this.indications = builder.indications;
  }

  public Long getId() {
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public void setTimeBlock(ThirtyMinuteBlock timeBlock) {
    this.timeBlock = timeBlock;
  }

  public void setStatus(AppointmentStatus status) {
    this.status = status;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCancelDesc(String cancelDesc) {
    this.cancelDesc = cancelDesc;
  }

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
        + ", indications="
        + indications
        + " "
        + patient
        + " "
        + doctor
        + "]";
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
    if (!(obj instanceof Appointment)) return false;
    Appointment other = (Appointment) obj;
    return Objects.equals(this.id, other.id);
  }

  public static class Builder {
    // required
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private ThirtyMinuteBlock timeBlock;
    private String description;

    // default
    private Long id = null;
    private String cancelDescription = null;
    private String indications = null;
    private AppointmentStatus status = AppointmentStatus.CONFIRMED;

    public Builder(
        Patient patient,
        Doctor doctor,
        LocalDate date,
        ThirtyMinuteBlock timeBlock,
        String description) {
      this.patient = patient;
      this.doctor = doctor;
      this.date = date;
      this.timeBlock = timeBlock;
      this.description = description;
    }

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder cancelDescription(String cancelDescription) {
      this.cancelDescription = cancelDescription;
      return this;
    }

    public Builder indications(String indications) {
      this.indications = indications;
      return this;
    }

    public Builder status(AppointmentStatus status) {
      this.status = status;
      return this;
    }

    public Appointment build() {
      return new Appointment(this);
    }
  }
}
