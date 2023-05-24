package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

  // =============== Inserts ===============

  Appointment createAppointment(
      Long patientId,
      Long doctorId,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description);

  // =============== Updates ===============

  Appointment updateAppointment(
      long appointmentId, AppointmentStatus status, String cancelDescription, long requesterId);

  // =============== Queries ===============

  Optional<Appointment> getAppointmentById(long appointmentId);

  List<Appointment> getAppointments(long userId, boolean isPatient);

  Page<Appointment> getFilteredAppointments(
      long userId, AppointmentStatus status, Integer page, Integer pageSize, boolean isPatient);

  public List<ThirtyMinuteBlock> getAvailableHoursForDoctorOnDate(long doctorId, LocalDate date);

  public List<List<ThirtyMinuteBlock>> getAvailableHoursForDoctorOnRange(
      long doctorId, LocalDate from, LocalDate to);

  public boolean hasPatientMetDoctor(long patientId, long doctorId);
  
  // ================ Tasks ================
  void sendAppointmentReminders();

  void updateCompletedAppointmentsStatus();
}
