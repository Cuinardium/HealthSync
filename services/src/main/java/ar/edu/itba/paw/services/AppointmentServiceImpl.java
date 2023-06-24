package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.CancelForbiddenException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotAvailableException;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.SetIndicationsForbiddenException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.models.Vacation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
  private final MailService mailService;
  private final DoctorService doctorService;
  private final PatientService patientService;

  private final AppointmentDao appointmentDao;

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

    if (!getAvailableHoursForDoctorOnDate(doctorId, date).contains(timeBlock)) {
      throw new DoctorNotAvailableException();
    }

    try {
      // Create appointment
      Appointment appointment =
          appointmentDao.createAppointment(patient, doctor, date, timeBlock, description);

      // TODO: locale should be determined by the user's language
      mailService.sendAppointmentRequestMail(appointment);
      mailService.sendAppointmentReminderMail(appointment);

      return appointment;
    } catch (AppointmentAlreadyExistsException e) {
      throw new DoctorNotAvailableException();
    }
  }

  // =============== Updates ===============

  @Transactional
  @Override
  public Appointment cancelAppointment(
      long appointmentId, String cancelDescription, long requesterId)
      throws AppointmentNotFoundException, CancelForbiddenException {

    // Get appointment
    Appointment appointment =
        getAppointmentById(appointmentId).orElseThrow(AppointmentNotFoundException::new);

    // If requester is nor the patient nor the doctor, he can't update the appointment
    if (requesterId != appointment.getPatientId() && requesterId != appointment.getDoctorId()) {
      throw new CancelForbiddenException();
    }

    Appointment updatedAppointment;

    try {
      updatedAppointment =
          appointmentDao.updateAppointment(
              appointmentId, AppointmentStatus.CANCELLED, cancelDescription);
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException e) {
      throw new IllegalStateException("Appointment could not be updated due to it not existing");
    }

    if (requesterId == appointment.getPatientId()) {
      mailService.sendAppointmentCancelledByPatientMail(updatedAppointment);
    } else {
      mailService.sendAppointmentCancelledByDoctorMail(updatedAppointment);
    }

    return updatedAppointment;
  }

  @Transactional
  @Override
  public Appointment setAppointmentIndications(
      long appointmentId, String indications, long requesterId)
      throws AppointmentNotFoundException, SetIndicationsForbiddenException {
    Appointment appointment =
        getAppointmentById(appointmentId).orElseThrow(AppointmentNotFoundException::new);

    if (requesterId != appointment.getDoctorId()) {
      throw new SetIndicationsForbiddenException();
    }

    Appointment updatedAppointment;

    try {
      updatedAppointment = appointmentDao.setAppointmentIndications(appointmentId, indications);
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException e) {
      throw new IllegalStateException(
          "Appointment indications could not be updated due to it not existing");
    }

    mailService.sendAppointmentIndicationMail(appointment);

    return updatedAppointment;
  }

  // =============== Queries ===============

  @Transactional(readOnly = true)
  @Override
  public List<Appointment> getAppointments(long userId, boolean isPatient) {
    return appointmentDao.getAppointments(userId, isPatient);
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<Appointment> getAppointmentById(long appointmentId) {
    return appointmentDao.getAppointmentById(appointmentId);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ThirtyMinuteBlock> getAvailableHoursForDoctorOnDate(long doctorId, LocalDate date)
      throws DoctorNotFoundException {
    return getAvailableHoursForDoctorOnRange(doctorId, date, date).get(0);
  }

  @Transactional(readOnly = true)
  @Override
  public List<List<ThirtyMinuteBlock>> getAvailableHoursForDoctorOnRange(
      long doctorId, LocalDate from, LocalDate to) throws DoctorNotFoundException {

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);
    Set<Vacation> vacations = doctor.getVacations();

    // Get doctor appointments for range
    List<Appointment> appointments =
        appointmentDao
            .getFilteredAppointments(doctorId, null, from, to, null, null, false)
            .getContent();

    // Populate map from each date for range to available hours in date
    Map<LocalDate, List<ThirtyMinuteBlock>> availableHours = new HashMap<>();

    for (LocalDate date = from; date.isBefore(to.plusDays(1)); date = date.plusDays(1)) {
      List<ThirtyMinuteBlock> currentList = new ArrayList<>(doctor.getAttendingBlocksForDate(date));
      currentList.sort(Comparator.naturalOrder());
      availableHours.put(date, currentList);
    }

    // Remove booked hours from available hours in each date
    for (Appointment appointment : appointments) {
      if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {

        List<ThirtyMinuteBlock> availableHoursOnAppointmentDate =
            availableHours.get(appointment.getDate());

        if (availableHoursOnAppointmentDate != null) {
          availableHoursOnAppointmentDate.remove(appointment.getTimeBlock());
        }
      }
    }

    // Remove vacation days from available hours
    for (Vacation vacation : vacations) {

      LocalDate fromDate = vacation.getFromDate();
      LocalDate toDate = vacation.getToDate();

      for (LocalDate date = fromDate; date.isBefore(toDate.plusDays(1)); date = date.plusDays(1)) {

        List<ThirtyMinuteBlock> availableHoursOnVacationDate = availableHours.get(date);
        if (availableHoursOnVacationDate == null) {
          continue;
        }

        if (date.equals(fromDate)) {
          Collection<ThirtyMinuteBlock> unavailableHours =
              ThirtyMinuteBlock.fromRange(vacation.getFromTime(), ThirtyMinuteBlock.BLOCK_23_30);
          availableHoursOnVacationDate.removeAll(unavailableHours);

        } else if (date.equals(toDate)) {
          Collection<ThirtyMinuteBlock> unavailableHours =
              ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_00_00, vacation.getToTime());
          availableHoursOnVacationDate.removeAll(unavailableHours);

        } else {
          availableHoursOnVacationDate.clear();
        }
      }
    }

    return availableHours.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  @Override
  public Page<Appointment> getFilteredAppointments(
      long userId, AppointmentStatus status, Integer page, Integer pageSize, boolean isPatient) {
    return appointmentDao.getFilteredAppointments(
        userId, status, null, null, page, pageSize, isPatient);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<Appointment> getTodayAppointments(
      long userId, AppointmentStatus status, Integer page, Integer pageSize, boolean isPatient) {
    return appointmentDao.getFilteredAppointments(
        userId, status, LocalDate.now(), LocalDate.now().plusDays(1), page, pageSize, isPatient);
  }

  @Transactional(readOnly = true)
  @Override
  public boolean hasPatientMetDoctor(long patientId, long doctorId) {
    return appointmentDao.hasPatientMetDoctor(patientId, doctorId);
  }

  @Transactional(readOnly = true)
  @Override
  public boolean hasAppointmentsInRange(
      long doctorId,
      LocalDate fromDate,
      ThirtyMinuteBlock fromTime,
      LocalDate toDate,
      ThirtyMinuteBlock toTime) {

    List<Appointment> appointmentsInDateRange =
        appointmentDao
            .getFilteredAppointments(
                doctorId, AppointmentStatus.CONFIRMED, fromDate, toDate, null, null, false)
            .getContent();

    for (Appointment appointment : appointmentsInDateRange) {
      LocalDate appointmentDate = appointment.getDate();
      ThirtyMinuteBlock appointmentTimeBlock = appointment.getTimeBlock();

      if (appointmentDate.equals(fromDate) && appointmentTimeBlock.compareTo(fromTime) >= 0) {
        return true;
      }

      if (appointmentDate.equals(toDate) && appointmentTimeBlock.compareTo(toTime) <= 0) {
        return true;
      }

      // Probably unnecesary
      if (appointmentDate.isAfter(fromDate) && appointmentDate.isBefore(toDate)) {
        return true;
      }
    }

    return false;
  }

  // ======================================== TASKS ========================================

  // Run every 30 minutes
  @Transactional(readOnly = true)
  @Scheduled(cron = "0 0/30 * * * ?")
  @Override
  public void sendAppointmentReminders() {

    // Get tomorrow's date
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    // Get actual thirty minute block
    ThirtyMinuteBlock timeBlock = ThirtyMinuteBlock.fromTime(LocalTime.now());

    // Get all appointments for tomorrow
    List<Appointment> tomorrowAppointments =
        appointmentDao.getAllConfirmedAppointmentsInDateBlock(tomorrow, timeBlock);

    // For each appointment, send reminder to patient
    for (Appointment appointment : tomorrowAppointments) {
      mailService.sendAppointmentReminderMail(appointment);
    }
  }

  // Run every 30 minutes
  @Transactional(readOnly = true)
  @Scheduled(cron = "0 0/30 * * * ?")
  @Override
  public void updateCompletedAppointmentsStatus() {

    // Get yesterday's date
    LocalDate yesterday = LocalDate.now().minusDays(1);

    // Get actual thirty minute block
    ThirtyMinuteBlock timeBlock = ThirtyMinuteBlock.fromTime(LocalTime.now());

    // Complete all appointments for yesterday
    appointmentDao.completeAppointmentsInDateBlock(yesterday, timeBlock);

    // Get all appointments for yesterday
    List<Appointment> yesterdayAppointments =
        appointmentDao.getAllConfirmedAppointmentsInDateBlock(yesterday, timeBlock);

    // Send email to patient
    for (Appointment appointment : yesterdayAppointments) {
      mailService.sendAppointmentCompletedMail(appointment);
    }
  }
}
