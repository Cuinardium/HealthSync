package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotAvailableException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.ForbiddenCancelException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
  MailService mailService;
  DoctorService doctorService;
  PatientService patientService;

  AppointmentDao appointmentDao;

  @Autowired
  public AppointmentServiceImpl(
      MailService mailService,
      DoctorService doctorService,
      PatientService patientService,
      AppointmentDao appointmentDao) {
    this.mailService = mailService;
    this.doctorService = doctorService;
    this.patientService = patientService;
    this.appointmentDao = appointmentDao;
  }

  // =============== Inserts ===============

  @Transactional
  @Override
  public Appointment createAppointment(
      Long patientId,
      Long doctorId,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description)
      throws DoctorNotFoundException, PatientNotFoundException, DoctorNotAvailableException {

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);
    Patient patient =
        patientService.getPatientById(patientId).orElseThrow(PatientNotFoundException::new);

    // Check if doctor can attend in date and time block
    boolean attendsInDateBlock =
        doctor.getAttendingHours().getAttendingBlocksForDate(date).contains(timeBlock);

    if (!attendsInDateBlock) {
      throw new DoctorNotAvailableException();
    }

    Optional<Appointment> possibleAppointment =
        appointmentDao.getAppointment(doctorId, date, timeBlock);

    boolean isBooked =
        possibleAppointment.isPresent()
            && possibleAppointment.get().getStatus() == AppointmentStatus.CONFIRMED;

    if (isBooked) {
      throw new DoctorNotAvailableException();
    }

    // Create appointment
    Appointment appointment =
        appointmentDao.createAppointment(patient, doctor, date, timeBlock, description);

    // TODO: locale should be determined by the user's language
    mailService.sendAppointmentRequestMail(appointment, LocaleContextHolder.getLocale());
    mailService.sendAppointmentReminderMail(appointment, LocaleContextHolder.getLocale());

    return appointment;
  }

  // =============== Updates ===============

  @Transactional
  @Override
  public Appointment cancelAppointment(
      long appointmentId, String cancelDescription, long requesterId)
      throws AppointmentNotFoundException, ForbiddenCancelException {

    // Get appointment
    Appointment appointment = getAppointmentById(appointmentId).orElseThrow(AppointmentNotFoundException::new);

    // If requester is nor the patient nor the doctor, he can't update the appointment
    if (requesterId != appointment.getPatientId() && requesterId != appointment.getDoctorId()) {
      throw new ForbiddenCancelException();
    }

    Appointment updatedAppointment;

    try {
      updatedAppointment =
          appointmentDao.updateAppointment(
              appointmentId, AppointmentStatus.CANCELLED, cancelDescription);
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException e) {
      throw new IllegalStateException();
    }

    // TODO: locale should be determined by the user's language
    Locale locale = LocaleContextHolder.getLocale();

    if (requesterId == appointment.getPatientId()) {
      mailService.sendAppointmentCancelledByPatientMail(updatedAppointment, locale);
    } else {
      mailService.sendAppointmentCancelledByDoctorMail(updatedAppointment, locale);
    }

    return updatedAppointment;
  }

  // =============== Queries ===============

  @Override
  public List<Appointment> getAppointments(long userId, boolean isPatient) {
    return appointmentDao.getAppointments(userId, isPatient);
  }

  // ============================= QUERIES =============================

  @Override
  public Optional<Appointment> getAppointmentById(long appointmentId) {
    return appointmentDao.getAppointmentById(appointmentId);
  }

  @Override
  public List<ThirtyMinuteBlock> getAvailableHoursForDoctorOnDate(long doctorId, LocalDate date) throws DoctorNotFoundException {
    return getAvailableHoursForDoctorOnRange(doctorId, date, date).get(0);
  }

  @Override
  public List<List<ThirtyMinuteBlock>> getAvailableHoursForDoctorOnRange(
      long doctorId, LocalDate from, LocalDate to) throws DoctorNotFoundException {

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    // Get doctor appointments for range
    List<Appointment> appointments =
        appointmentDao.getFilteredAppointments(doctorId, null, from, to, null, null, false).getContent();

    // Populate map from each date for range to available hours in date
    Map<LocalDate, List<ThirtyMinuteBlock>> availableHours = new HashMap<>();

    for (LocalDate date = from; date.isBefore(to.plusDays(1)); date = date.plusDays(1)) {
      List<ThirtyMinuteBlock> currentList = new ArrayList<>(doctor.getAttendingHours().getAttendingBlocksForDate(date));
      availableHours.put(date, currentList);
    }

    // Remove booked hours from available hours in each date
    for (Appointment appointment : appointments) {
      if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
        
        List<ThirtyMinuteBlock> availableHoursOnAppointmentDate = availableHours.get(appointment.getDate());

        if (availableHoursOnAppointmentDate == null) {
          throw new IllegalStateException();
        }

        availableHoursOnAppointmentDate.remove(appointment.getTimeBlock());
      }
    }

    return new ArrayList<>(availableHours.values());
  }

  @Override
  public Page<Appointment> getFilteredAppointments(
      long userId, AppointmentStatus status, Integer page, Integer pageSize, boolean isPatient) {
    return appointmentDao.getFilteredAppointments(
        userId, status, null, null, page, pageSize, isPatient);
  }

  @Override
  public boolean hasPatientMetDoctor(long patientId, long doctorId) {
    return appointmentDao.hasPatientMetDoctor(patientId, doctorId);
  }

  // ======================================== TASKS ========================================

  // Run at midnight every day
  @Scheduled(cron = "0 0 0 * * ?")
  @Override
  public void sendAppointmentReminders() {

    // Get tomorrow's date
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    // Get all appointments for tomorrow
    List<Appointment> tomorrowAppointments =
        appointmentDao.getAllConfirmedAppointmentsInDate(tomorrow);

    // For each appointment, send reminder to patient
    for (Appointment appointment : tomorrowAppointments) {
      mailService.sendAppointmentReminderMail(appointment, LocaleContextHolder.getLocale());
    }
  }

  // Run at midnight every day
  @Scheduled(cron = "0 0 0 * * ?")
  @Override
  public void updateCompletedAppointmentsStatus() {

    // Get yesterday's date
    LocalDate yesterday = LocalDate.now().minusDays(1);

    // Complete all appointments for yesterday
    appointmentDao.completeAppointmentsInDate(yesterday);

    // Get all appointments for yesterday
    List<Appointment> yesterdayAppointments =
        appointmentDao.getAllConfirmedAppointmentsInDate(yesterday);

    // Send email to patient
    for (Appointment appointment : yesterdayAppointments) {
      mailService.sendAppointmentCompletedMail(appointment, LocaleContextHolder.getLocale());
    }
  }
}
