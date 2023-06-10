package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorServiceImpl implements DoctorService {
  private final DoctorDao doctorDao;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Autowired
  public DoctorServiceImpl(
      DoctorDao doctorDao, UserService userService, PasswordEncoder passwordEncoder) {

    this.doctorDao = doctorDao;

    this.userService = userService;

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
      City city,
      String address,
      Set<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours)
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
                attendingHours)
            .build();

    try {
      return doctorDao.createDoctor(doctor);
    } catch (DoctorAlreadyExistsException e) {
      throw new IllegalStateException("Doctor should not exist when id is null");
    }
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
      City city,
      String address,
      Set<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours,
      Image image)
      throws DoctorNotFoundException, EmailInUseException {
    try {
      userService.updateUser(doctorId, email, firstName, lastName, image);
      return doctorDao.updateDoctorInfo(
          doctorId, specialty, city, address, healthInsurances, attendingHours);
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException
        | UserNotFoundException e) {
      throw new DoctorNotFoundException();
    }
  }

  // =============== Queries ===============

  @Transactional
  @Override
  public Optional<Doctor> getDoctorById(long id) {
    return doctorDao.getDoctorById(id);
  }

  @Transactional
  @Override
  public Page<Doctor> getFilteredDoctors(
      String name,
      LocalDate date,
      ThirtyMinuteBlock fromTime,
      ThirtyMinuteBlock toTime,
      Specialty specialty,
      City city,
      HealthInsurance healthInsurance,
      Integer page,
      Integer pageSize) {
    return doctorDao.getFilteredDoctors(
        name, date, fromTime, toTime, specialty, city, healthInsurance, page, pageSize);
  }

  @Transactional
  @Override
  public List<Doctor> getDoctors() {
    return doctorDao.getDoctors();
  }

  // Get all Specialties and health insurances that are used by doctors
  @Transactional
  @Override
  public Map<HealthInsurance, Integer> getUsedHealthInsurances() {
    return doctorDao.getUsedHealthInsurances();
  }

  @Transactional
  @Override
  public Map<Specialty, Integer> getUsedSpecialties() {
    return doctorDao.getUsedSpecialties();
  }

  @Transactional
  @Override
  public Map<City, Integer> getUsedCities() {
    return doctorDao.getUsedCities();
  }
}
