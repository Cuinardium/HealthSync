package ar.edu.itba.paw.interfaces.persistence;

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
      String description);

  // =============== Updates ===============

  Appointment updateAppointment(
      long appointmentId, AppointmentStatus status, String cancelDescription)
      throws AppointmentNotFoundException;

  void completeAppointmentsInDate(LocalDate date);

  // =============== Queries ===============

  Optional<Appointment> getAppointmentById(long appointmentId);

  Optional<Appointment> getAppointment(long doctorId, LocalDate date, ThirtyMinuteBlock timeBlock);

  List<Appointment> getAppointments(long userId, boolean isPatient);

  Page<Appointment> getFilteredAppointments(
      long userId,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      Integer page,
      Integer pageSize,
      boolean isPatient);

  boolean hasPatientMetDoctor(long patientId, long doctorId);

  List<Appointment> getAllConfirmedAppointmentsInDate(LocalDate date);
}
