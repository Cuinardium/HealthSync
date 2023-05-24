package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
      throws IllegalStateException {

    Optional<Appointment> possibleAppointment =
        appointmentDao.getAppointment(doctorId, date, timeBlock);

    if (possibleAppointment.isPresent()
        && (possibleAppointment.get().getStatus() == AppointmentStatus.COMPLETED
            || possibleAppointment.get().getStatus() == AppointmentStatus.CONFIRMED)) {
      throw new RuntimeException();
    }

    Patient patient =
        patientService.getPatientById(patientId).orElseThrow(IllegalStateException::new);
    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(IllegalStateException::new);
    Appointment appointment =
        appointmentDao.createAppointment(patient, doctor, date, timeBlock, description);

    // TODO: locale should be determined by the patient's language
    mailService.sendAppointmentRequestMail(appointment, LocaleContextHolder.getLocale());
    mailService.sendAppointmentReminderMail(appointment, LocaleContextHolder.getLocale());

    return appointment;
  }

  // =============== Updates ===============

  @Transactional
  @Override
  public Appointment updateAppointment(
      long appointmentId, AppointmentStatus status, String cancelDescription, long requesterId) {

    // Get appointment
    // TODO: error handling
    Appointment appointment = getAppointmentById(appointmentId).orElseThrow(RuntimeException::new);

    // If requester is nor the patient nor the doctor, he can't update the appointment
    if (requesterId != appointment.getPatientId() && requesterId != appointment.getDoctorId()) {
      throw new RuntimeException();
    }

    Appointment updatedAppointment;

    try {
      updatedAppointment =
          appointmentDao.updateAppointment(appointmentId, status, cancelDescription);
    } catch (AppointmentNotFoundException e) {
      throw new RuntimeException();
    }

    Locale locale = LocaleContextHolder.getLocale();

    if (status == AppointmentStatus.CANCELLED) {
      if (requesterId == appointment.getPatientId()) {
        mailService.sendAppointmentCancelledByPatientMail(updatedAppointment, locale);
      } else {
        mailService.sendAppointmentCancelledByDoctorMail(updatedAppointment, locale);
      }
    } else if (status == AppointmentStatus.COMPLETED) {
      mailService.sendAppointmentCompletedMail(updatedAppointment, locale);
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
  public List<ThirtyMinuteBlock> getAvailableHoursForDoctorOnDate(long doctorId, LocalDate date) {
    return getAvailableHoursForDoctorOnRange(doctorId, date, date).get(0);
  }

  @Override
  public List<List<ThirtyMinuteBlock>> getAvailableHoursForDoctorOnRange(
      long doctorId, LocalDate from, LocalDate to) {
    // Get doctor appointments for date
    Page<Appointment> appointments =
        appointmentDao.getFilteredAppointments(doctorId, null, from, to, -1, -1, false);

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(RuntimeException::new);

    // Get doctor available hours for date
    List<List<ThirtyMinuteBlock>> availableHours = new ArrayList<>();
    int i = 0;
    for (LocalDate date = from; date.isBefore(to.plusDays(1)); date = date.plusDays(1)) {
      availableHours.add(new ArrayList<>());
      List<ThirtyMinuteBlock> currentList = availableHours.get(i);
      currentList.addAll(doctor.getAttendingHours().getAttendingBlocksForDate(date));
      i++;
    }

    i = 0;
    List<ThirtyMinuteBlock> currentList = availableHours.get(i);
    LocalDate currentDate = LocalDate.of(from.getYear(), from.getMonth(), from.getDayOfMonth());
    for (Appointment appointment : appointments.getContent()) {
      if ((appointment.getStatus() == AppointmentStatus.CONFIRMED
          || appointment.getStatus() == AppointmentStatus.COMPLETED)) {
        currentList.remove(appointment.getTimeBlock());
      }
      if (appointment.getDate().isAfter(currentDate)) {
        i++;
        currentList = availableHours.get(i);
        currentDate = currentDate.plusDays(1);
      }
    }

    return availableHours;
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
    List<Appointment> tomorrowAppointments = appointmentDao.getAllConfirmedAppointmentsInDate(tomorrow);

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
    List<Appointment> yesterdayAppointments = appointmentDao.getAllConfirmedAppointmentsInDate(yesterday);

    // Send email to patient
    for (Appointment appointment : yesterdayAppointments) {
      mailService.sendAppointmentCompletedMail(appointment, LocaleContextHolder.getLocale());
    }
  }
}
