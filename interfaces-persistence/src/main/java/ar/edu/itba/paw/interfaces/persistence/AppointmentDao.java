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

  Appointment createAppointment(
      Patient patient,
      Doctor doctor,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description)
      throws AppointmentAlreadyExistsException;

  // =============== Updates ===============

  Appointment updateAppointment(
      long appointmentId, AppointmentStatus status, String cancelDescription)
      throws AppointmentNotFoundException;

  void completeAppointmentsInDateBlock(LocalDate date, ThirtyMinuteBlock block);

  // =============== Queries ===============

  Optional<Appointment> getAppointmentById(long appointmentId);

  Optional<Appointment> getAppointment(long doctorId, LocalDate date, ThirtyMinuteBlock timeBlock);

  List<Appointment> getAppointments(long userId, boolean isPatient);

  Page<Appointment> getFilteredAppointments(
      Long userId,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      Integer page,
      Integer pageSize,
      Boolean isPatient);

  boolean hasPatientMetDoctor(long patientId, long doctorId);

  List<Appointment> getAllConfirmedAppointmentsInDateBlock(LocalDate date, ThirtyMinuteBlock block);
}
