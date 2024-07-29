package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.VacationCollisionException;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.TokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.*;
import ar.edu.itba.paw.models.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorServiceImpl implements DoctorService {
  private final DoctorDao doctorDao;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final TokenService tokenService;
  private final MailService mailService;

  @Autowired
  public DoctorServiceImpl(
      DoctorDao doctorDao,
      UserService userService,
      TokenService tokenService,
      MailService mailService,
      PasswordEncoder passwordEncoder) {

    this.doctorDao = doctorDao;

    this.userService = userService;
    this.tokenService = tokenService;
    this.mailService = mailService;

    this.passwordEncoder = passwordEncoder;
  }

  // =============== Inserts ===============

  @Transactional
  @Override
  public Doctor createDoctor(
      String email,
      String password,
      String firstName,
      String lastName,
      Specialty specialty,
      String city,
      String address,
      Set<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours,
      Locale locale)
      throws EmailInUseException {

    if (userService.getUserByEmail(email).isPresent()) {
      throw new EmailInUseException();
    }

    Doctor doctor =
        new Doctor.Builder(
                email,
                passwordEncoder.encode(password),
                firstName,
                lastName,
                healthInsurances,
                specialty,
                city,
                address,
                attendingHours,
                locale)
            .build();

    try {
      doctor = doctorDao.createDoctor(doctor);
    } catch (DoctorAlreadyExistsException e) {
      throw new IllegalStateException("Doctor should not exist when id is null");
    }

    User patientUser =
        userService
            .getUserById(doctor.getId())
            .orElseThrow(() -> new IllegalStateException("User should exist but does not"));
    final VerificationToken token = tokenService.createToken(patientUser);
    mailService.sendConfirmationMail(token);

    return doctor;
  }

  // =============== Updates ===============

  @Transactional
  @Override
  public Doctor updateDoctor(
      long doctorId,
      String email,
      String firstName,
      String lastName,
      Specialty specialty,
      String city,
      String address,
      Set<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours,
      Image image,
      Locale locale)
      throws DoctorNotFoundException, EmailInUseException {
    try {
      userService.updateUser(doctorId, email, firstName, lastName, image, locale);
      return doctorDao.updateDoctorInfo(
          doctorId, specialty, city, address, healthInsurances, attendingHours);
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException
        | UserNotFoundException e) {
      throw new DoctorNotFoundException();
    }
  }

  @Transactional
  @Override
  public Vacation addVacation(long doctorId, Vacation vacation)
      throws DoctorNotFoundException, VacationInvalidException {

    LocalDate fromDate = vacation.getFromDate();
    LocalDate toDate = vacation.getToDate();
    ThirtyMinuteBlock fromTime = vacation.getFromTime();
    ThirtyMinuteBlock toTime = vacation.getToTime();

    boolean fromIsAfterTo =
        fromDate.isAfter(toDate) || (fromDate.isEqual(toDate) && fromTime.isAfter(toTime));

    LocalDate today = LocalDate.now();
    ThirtyMinuteBlock now = ThirtyMinuteBlock.fromTime(LocalTime.now());

    boolean fromIsBeforeNow =
        fromDate.isBefore(today) || (fromDate.isEqual(today) && fromTime.isBefore(now));

    if (fromIsAfterTo || fromIsBeforeNow) {
      throw new VacationInvalidException();
    }

    try {
      return doctorDao.addVacation(doctorId, vacation);
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException e) {
      throw new DoctorNotFoundException();
    } catch (VacationCollisionException e) {
      throw new VacationInvalidException();
    }
  }

  @Transactional
  @Override
  public Doctor removeVacation(long doctorId, Vacation vacation)
      throws DoctorNotFoundException, VacationNotFoundException {
    try {
      return doctorDao.removeVacation(doctorId, vacation);
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException e) {
      throw new DoctorNotFoundException();
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.VacationNotFoundException e) {
      throw new VacationNotFoundException();
    }
  }

  // =============== Queries ===============

  @Transactional(readOnly = true)
  @Override
  public Optional<Doctor> getDoctorById(long id) {
    return doctorDao.getDoctorById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<Doctor> getFilteredDoctors(
      String name,
      LocalDate date,
      ThirtyMinuteBlock fromTime,
      ThirtyMinuteBlock toTime,
      Set<Specialty> specialties,
      Set<String> cities,
      Set<HealthInsurance> healthInsurances,
      Integer minRating,
      Integer page,
      Integer pageSize) {
    return doctorDao.getFilteredDoctors(
        name,
        date,
        fromTime,
        toTime,
        specialties,
        cities,
        healthInsurances,
        minRating,
        page,
        pageSize);
  }

  @Transactional(readOnly = true)
  @Override
  public List<Doctor> getDoctors() {
    return doctorDao.getDoctors();
  }

  // Get all Specialties and health insurances that are used by doctors
  @Transactional(readOnly = true)
  @Override
  public Map<HealthInsurance, Integer> getUsedHealthInsurances() {
    return doctorDao.getUsedHealthInsurances();
  }

  @Transactional(readOnly = true)
  @Override
  public Map<Specialty, Integer> getUsedSpecialties() {
    return doctorDao.getUsedSpecialties();
  }

  @Transactional(readOnly = true)
  @Override
  public Map<String, Integer> getUsedCities() {
    return doctorDao.getUsedCities();
  }

  @Transactional(readOnly = true)
  @Override
  public List<Specialty> getPopularSpecialties() {
    return doctorDao.getPopularSpecialties();
  }

  // ================= Tasks =================

  // Deletes all vacations that have ended
  @Transactional
  @Scheduled(cron = "0 0/30 * * * ?")
  @Override
  public void deleteOldVacations() {

    // Get today's date
    LocalDate today = LocalDate.now();

    // Get actual thirty minute block
    ThirtyMinuteBlock now = ThirtyMinuteBlock.fromTime(LocalTime.now());

    // Delete all vacations that have ended
    doctorDao.deleteOldVacations(today, now);
  }
}
