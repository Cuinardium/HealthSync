package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorServiceImpl implements DoctorService {
  private final DoctorDao doctorDao;
  private final UserService userService;

  @Autowired
  public DoctorServiceImpl(DoctorDao doctorDao, UserService userService) {

    this.doctorDao = doctorDao;

    this.userService = userService;
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
      List<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours)
      throws IllegalStateException {
    try {
      // Create user
//      User user = userService.createUser(email, password, firstName, lastName);
      // Create doctor
      return doctorDao.createDoctor(new Doctor(null, email, password, firstName, lastName, new Image(null, null), healthInsurances, specialty, new Location(city, address) , attendingHours, 0f ,0 ));
    } catch (DoctorAlreadyExistsException e) {
      throw new IllegalStateException();
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
      List<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours,
      Image image)
      throws UserNotFoundException {

    try {
      userService.updateUser(doctorId, email, firstName, lastName, image);
      return doctorDao.updateDoctorInfo(
          doctorId, specialty, city, address, healthInsurances, attendingHours);
    } catch (DoctorNotFoundException e) {
      throw new RuntimeException();
    }
  }

  // =============== Queries ===============

  @Override
  public Optional<Doctor> getDoctorById(long id) {
    return doctorDao.getDoctorById(id);
  }

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

  @Override
  public List<Doctor> getDoctors() {
    return doctorDao.getDoctors();
  }

  // Get all Specialties and health insurances that are used by doctors
  @Override
  public Map<HealthInsurance, Integer> getUsedHealthInsurances() {
    return doctorDao.getUsedHealthInsurances();
  }

  @Override
  public Map<Specialty, Integer> getUsedSpecialties() {
    return doctorDao.getUsedSpecialties();
  }

  @Override
  public Map<City, Integer> getUsedCities() {
    return doctorDao.getUsedCities();
  }
}
