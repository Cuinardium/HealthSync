package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {

  Appointment createAppointment(
      long patientId,
      long doctorId,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description);

  Optional<Appointment> getAppointmentById(long appointmentId);

  List<Appointment> getAppointmentsForPatient(long patientId);

  List<Appointment> getAppointmentsForDoctor(long doctorId);

  List<Appointment> getAppointmentsForPatientByStatus(long patientId, AppointmentStatus status);

  List<Appointment> getAppointmentsForDoctorByStatus(long doctorId, AppointmentStatus status);

  void updateAppointmentStatus(long appointmentId, AppointmentStatus status);
}
