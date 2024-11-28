package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {

  // =============== Inserts ===============

  public Appointment createAppointment(
      Patient patient,
      Doctor doctor,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description)
      throws AppointmentAlreadyExistsException;

  // =============== Updates ===============

  public Appointment updateAppointment(
      long appointmentId, AppointmentStatus status, String cancelDescription)
      throws AppointmentNotFoundException;


  public void completeAppointmentsInDateBlock(LocalDate date, ThirtyMinuteBlock block);

  // =============== Queries ===============

  public Optional<Appointment> getAppointmentById(long appointmentId);

  public Optional<Appointment> getAppointment(
      long doctorId, LocalDate date, ThirtyMinuteBlock timeBlock);

  public List<Appointment> getAppointments(long userId, boolean isPatient);

  public Page<Appointment> getFilteredAppointments(
      Long userId,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      Integer page,
      Integer pageSize,
      Boolean sortAsc,
      Boolean isPatient);

  public boolean hasPatientMetDoctor(long patientId, long doctorId);

  public boolean hasAppointmentWithPatient(long doctorId, long patientId);

  public List<Appointment> getAllConfirmedAppointmentsInDateBlock(
      LocalDate date, ThirtyMinuteBlock block);
}
