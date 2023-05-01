package ar.edu.itba.paw.interfaces.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;

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

  void updateAppointmentStatus(long appointmentId, AppointmentStatus status);
}
