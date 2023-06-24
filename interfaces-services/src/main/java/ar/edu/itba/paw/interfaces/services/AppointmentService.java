package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.CancelForbiddenException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotAvailableException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.SetIndicationsForbiddenException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

  // =============== Inserts ===============

  public Appointment createAppointment(
      Long patientId,
      Long doctorId,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description)
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException;

  // =============== Updates ===============

  public Appointment cancelAppointment(
      long appointmentId, String cancelDescription, long requesterId)
      throws AppointmentNotFoundException, CancelForbiddenException;

  public Appointment setAppointmentIndications(
      long appointmentId, String indications, long requesterId)
      throws AppointmentNotFoundException, SetIndicationsForbiddenException;

  // =============== Queries ===============

  public Optional<Appointment> getAppointmentById(long appointmentId);

  public List<Appointment> getAppointments(long userId, boolean isPatient);

  public Page<Appointment> getFilteredAppointments(
      long userId, AppointmentStatus status, Integer page, Integer pageSize, boolean isPatient);

  Page<Appointment> getTodayAppointments(
      long userId, AppointmentStatus status, Integer page, Integer pageSize, boolean isPatient);

  public List<ThirtyMinuteBlock> getAvailableHoursForDoctorOnDate(long doctorId, LocalDate date)
      throws DoctorNotFoundException;

  public List<List<ThirtyMinuteBlock>> getAvailableHoursForDoctorOnRange(
      long doctorId, LocalDate from, LocalDate to) throws DoctorNotFoundException;

  public boolean hasPatientMetDoctor(long patientId, long doctorId);

  public boolean hasAppointmentsInRange(
      long doctorId,
      LocalDate fromDate,
      ThirtyMinuteBlock fromTime,
      LocalDate toDate,
      ThirtyMinuteBlock toTime);

  // ================ Tasks ================
  public void sendAppointmentReminders();

  public void updateCompletedAppointmentsStatus();
}
