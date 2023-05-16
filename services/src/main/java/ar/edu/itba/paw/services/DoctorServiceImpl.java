package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
      AttendingHours attendingHours) {

    // Create user
    User user = userService.createUser(email, password, firstName, lastName);

    // Create doctor
    return doctorDao.createDoctor(
        user.getId(), specialty, city, address, healthInsurances, attendingHours);
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
      AttendingHours attendingHours,
      Image image) {

    userService.updateUser(doctorId, email, firstName, lastName, image);

    return doctorDao.updateDoctorInfo(
        doctorId, specialty, city, address, healthInsurances, attendingHours);
  }

  // =============== Queries ===============

  @Override
  public Optional<Doctor> getDoctorById(long id) {
    return doctorDao.getDoctorById(id);
  }

  @Override
  public Page<Doctor> getFilteredDoctors(
      String name,
      Specialty specialty,
      City city,
      HealthInsurance healthInsurance,
      int page,
      int pageSize) {
    return doctorDao.getFilteredDoctors(name, specialty, city, healthInsurance, page, pageSize);
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
