package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.AppointmentService;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.exceptions.*;
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
      throws DoctorNotFoundException,
          PatientNotFoundException,
          DoctorNotAvailableException,
          PatientNotAvailableException,
          AppointmentInPastException {

    LocalDate today = LocalDate.now();
    ThirtyMinuteBlock now = ThirtyMinuteBlock.fromTime(LocalTime.now());

    boolean appointmentInPast =
        date.isBefore(today) || (date.isEqual(today) && !timeBlock.isAfter(now));

    if (appointmentInPast) {
      throw new AppointmentInPastException();
    }

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    if (!doctor.getIsVerified()) {
      throw new DoctorNotFoundException();
    }

    Patient patient =
        patientService.getPatientById(patientId).orElseThrow(PatientNotFoundException::new);

    List<Appointment> patientAppointmentsInDate =
        appointmentDao
            .getFilteredAppointments(
                patientId, AppointmentStatus.CONFIRMED, date, date, null, null, true, true)
            .getContent();

    boolean isPatientOccupied =
        patientAppointmentsInDate.stream()
            .anyMatch(appointment -> appointment.getTimeBlock().equals(timeBlock));

    if (isPatientOccupied) {
      throw new PatientNotAvailableException();
    }

    if (!getAvailableHoursForDoctorOnDate(doctorId, date).contains(timeBlock)) {
      throw new DoctorNotAvailableException();
    }

    try {
      // Create appointment
      Appointment appointment =
          appointmentDao.createAppointment(patient, doctor, date, timeBlock, description);

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
      throws AppointmentNotFoundException, CancelForbiddenException, AppointmentInmutableException {

    // Get appointment
    Appointment appointment =
        getAppointmentById(appointmentId).orElseThrow(AppointmentNotFoundException::new);

    // If requester is nor the patient nor the doctor, he can't update the appointment
    if (requesterId != appointment.getPatientId() && requesterId != appointment.getDoctorId()) {
      throw new CancelForbiddenException();
    }

    if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
      throw new AppointmentInmutableException();
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
  public void cancelAppointmentsInRange(
      long doctorId,
      LocalDate fromDate,
      ThirtyMinuteBlock fromTime,
      LocalDate toDate,
      ThirtyMinuteBlock toTime,
      String cancelDescription)
      throws DoctorNotFoundException {

    doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    List<Appointment> appointmentsInDateRange =
        appointmentDao
            .getFilteredAppointments(
                doctorId, AppointmentStatus.CONFIRMED, fromDate, toDate, null, null, true, false)
            .getContent();
    try {
      for (Appointment appointment : appointmentsInDateRange) {
        LocalDate appointmentDate = appointment.getDate();
        ThirtyMinuteBlock appointmentTimeBlock = appointment.getTimeBlock();

        if (appointmentDate.equals(fromDate) && appointmentTimeBlock.compareTo(fromTime) >= 0) {
          cancelAppointment(appointment.getId(), cancelDescription, doctorId);
        }

        if (appointmentDate.equals(toDate) && appointmentTimeBlock.compareTo(toTime) <= 0) {
          cancelAppointment(appointment.getId(), cancelDescription, doctorId);
        }

        // Probably unnecesary
        if (appointmentDate.isAfter(fromDate) && appointmentDate.isBefore(toDate)) {
          cancelAppointment(appointment.getId(), cancelDescription, doctorId);
        }
      }
    } catch (AppointmentNotFoundException
        | CancelForbiddenException
        | AppointmentInmutableException e) {
      throw new IllegalStateException("Appointment could not be cancelled");
    }
  }

  // =============== Queries ===============

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
            .getFilteredAppointments(doctorId, null, from, to, null, null, true, false)
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

        if (date.equals(fromDate) && !date.equals(toDate)) {
          Collection<ThirtyMinuteBlock> unavailableHours =
              ThirtyMinuteBlock.fromRange(vacation.getFromTime(), ThirtyMinuteBlock.BLOCK_23_30);
          availableHoursOnVacationDate.removeAll(unavailableHours);

        } else if (date.equals(toDate) && !date.equals(fromDate)) {
          Collection<ThirtyMinuteBlock> unavailableHours =
              ThirtyMinuteBlock.fromRange(ThirtyMinuteBlock.BLOCK_00_00, vacation.getToTime());
          availableHoursOnVacationDate.removeAll(unavailableHours);

        } else if (date.equals(fromDate) && date.equals(toDate)) {
          Collection<ThirtyMinuteBlock> unavailableHours =
              ThirtyMinuteBlock.fromRange(vacation.getFromTime(), vacation.getToTime());
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
  public Map<LocalDate, List<ThirtyMinuteBlock>> getOccupiedHours(
      long doctorId, LocalDate from, LocalDate to)
      throws DoctorNotFoundException, InvalidRangeException {

    if (from == null || to == null || from.isBefore(LocalDate.now()) || from.isAfter(to)) {
      throw new InvalidRangeException();
    }

    Doctor doctor = doctorService.getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);

    if (!doctor.getIsVerified()) {
      throw new DoctorNotFoundException();
    }

    Map<LocalDate, List<ThirtyMinuteBlock>> occupiedHours = new HashMap<>();

    List<Vacation> vacationsInRange =
        doctor.getVacations().stream()
            .filter(vacation -> vacation.isDuring(from, to))
            .collect(Collectors.toList());

    // Add vacation days to occupied hours
    for (Vacation vacation : vacationsInRange) {
      Map<LocalDate, List<ThirtyMinuteBlock>> datesAndTimes = vacation.getDatesAndTimes(from, to);

      datesAndTimes.forEach(
          (date, times) ->
              occupiedHours.computeIfAbsent(date, k -> new ArrayList<>()).addAll(times));
    }

    List<Appointment> confirmedAppointments =
        appointmentDao
            .getFilteredAppointments(
                doctorId, AppointmentStatus.CONFIRMED, from, to, null, null, true, false)
            .getContent();

    // Add appointments to occupied hours
    for (Appointment appointment : confirmedAppointments) {
      occupiedHours
          .computeIfAbsent(appointment.getDate(), k -> new ArrayList<>())
          .add(appointment.getTimeBlock());
    }

    return occupiedHours;
  }

  @Transactional(readOnly = true)
  @Override
  public Page<Appointment> getFilteredAppointments(
      long userId,
      AppointmentStatus status,
      LocalDate date,
      Integer page,
      Integer pageSize,
      Boolean sortAsc) {

    boolean isPatient = patientService.getPatientById(userId).isPresent();

    LocalDate to = date != null ? date.plusDays(1) : null;

    return appointmentDao.getFilteredAppointments(
        userId, status, date, to, page, pageSize, sortAsc, isPatient);
  }

  @Transactional(readOnly = true)
  @Override
  public boolean hasPatientMetDoctor(long patientId, long doctorId) {
    return appointmentDao.hasPatientMetDoctor(patientId, doctorId);
  }

  @Transactional(readOnly = true)
  @Override
  public boolean hasAppointmentWithPatient(long doctorId, long patientId) {
    return appointmentDao.hasAppointmentWithPatient(doctorId, patientId);
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
  @Transactional
  @Scheduled(cron = "0 0/30 * * * ?")
  @Override
  public void updateCompletedAppointmentsStatus() {

    // Get yesterday's date
    LocalDate yesterday = LocalDate.now().minusDays(1);

    // Get actual thirty minute block
    ThirtyMinuteBlock timeBlock = ThirtyMinuteBlock.fromTime(LocalTime.now());

    // Get all appointments for yesterday
    List<Appointment> yesterdayAppointments =
        appointmentDao.getAllConfirmedAppointmentsInDateBlock(yesterday, timeBlock);

    // Complete all appointments for yesterday
    appointmentDao.completeAppointmentsInDateBlock(yesterday, timeBlock);

    // Send email to patient
    for (Appointment appointment : yesterdayAppointments) {
      mailService.sendAppointmentCompletedMail(appointment);
    }
  }
}
