package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.*;
import java.time.LocalDate;
import java.util.*;

public interface AppointmentService {

  // =============== Inserts ===============

  public Appointment createAppointment(
      Long patientId,
      Long doctorId,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description)
      throws DoctorNotFoundException,
          PatientNotFoundException,
          DoctorNotAvailableException,
          PatientNotAvailableException,
          AppointmentInPastException;

  // =============== Updates ===============

  public Appointment cancelAppointment(
      long appointmentId, String cancelDescription, long requesterId)
      throws AppointmentNotFoundException, CancelForbiddenException, AppointmentInmutableException;

  public void cancelAppointmentsInRange(
      long doctorId,
      LocalDate fromDate,
      ThirtyMinuteBlock fromTime,
      LocalDate toDate,
      ThirtyMinuteBlock toTime,
      String cancelDescription)
      throws DoctorNotFoundException;

  // =============== Queries ===============

  public Optional<Appointment> getAppointmentById(long appointmentId);

  public Page<Appointment> getFilteredAppointments(
      long userId,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      Integer page,
      Integer pageSize,
      Boolean sortAsc);

  public List<ThirtyMinuteBlock> getAvailableHoursForDoctorOnDate(long doctorId, LocalDate date)
      throws DoctorNotFoundException;

  public List<List<ThirtyMinuteBlock>> getAvailableHoursForDoctorOnRange(
      long doctorId, LocalDate from, LocalDate to) throws DoctorNotFoundException;

  public Map<LocalDate, List<ThirtyMinuteBlock>> getOccupiedHours(
      long doctorId, LocalDate from, LocalDate to)
      throws DoctorNotFoundException, InvalidRangeException;

  public boolean hasPatientMetDoctor(long patientId, long doctorId);

  public boolean hasAppointmentWithPatient(long doctorId, long patientId);

  // ================ Tasks ================
  public void sendAppointmentReminders();

  public void updateCompletedAppointmentsStatus();
}
