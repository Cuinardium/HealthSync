package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

  Appointment createAppointment(
      long patientId,
      long doctorId,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description);

  Optional<Appointment> getAppointmentById(long appointmentId);

  List<Appointment> getAppointmentsForPatient(long patientId);

  List<Appointment> getAppointmentsForDoctor(long doctorId);

  Page<Appointment> getFilteredAppointmentsForPatient(long patientId, AppointmentStatus status, LocalDate from, LocalDate to, int page, int pageSize);

  Page<Appointment> getFilteredAppointmentsForDoctor(long doctorId, AppointmentStatus status, LocalDate from, LocalDate to, int page, int pageSize);

  void updateAppointmentStatus(long appointmentId, AppointmentStatus status, long requesterId);

  public List<ThirtyMinuteBlock> getAvailableHoursForDoctorOnDate(long doctorId, LocalDate date);
}
