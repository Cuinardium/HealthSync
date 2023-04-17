package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.HealthInsuranceService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.SpecialtyService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {

  private final DoctorDao doctorDao;

  private final UserService userService;
  private final SpecialtyService specialtyService;
  private final LocationService locationService;
  private final HealthInsuranceService healthInsuranceService;

  @Autowired
  public DoctorServiceImpl(
      DoctorDao doctorDao,
      UserService userService,
      SpecialtyService specialtyService,
      LocationService locationService,
      HealthInsuranceService healthInsuranceService) {

    this.doctorDao = doctorDao;

    this.userService = userService;
    this.specialtyService = specialtyService;
    this.locationService = locationService;
    this.healthInsuranceService = healthInsuranceService;
  }

  @Override
  public Doctor createDoctor(
      String email,
      String password,
      String firstName,
      String lastName,
      String healthInsurance,
      String specialty,
      String city,
      String address) {

    // Create user
    User user = userService.createUser(email, password, firstName, lastName);
    long userId = user.getId();
    long pfpId = user.getProfilePictureId();

    // Create specialty
    long specialtyId = specialtyService.createSpecialty(specialty).getId();

    // Create Location
    long locationId = locationService.createLocation(city, address).getId();

    // Create health insurance
    long healthInsuranceId = healthInsuranceService.createHealthInsurance(healthInsurance).getId();

    // Create doctor
    long doctorId = doctorDao.createDoctor(userId, specialtyId);

    // Add location to doctor
    doctorDao.addLocation(doctorId, locationId);

    // Add health insurance to doctor
    doctorDao.addHealthInsurance(doctorId, healthInsuranceId);

    return new Doctor(
        doctorId,
        email,
        password,
        firstName,
        lastName,
        pfpId,
        healthInsurance,
        specialty,
        city,
        address);
  }

  @Override
  public Optional<Doctor> getDoctorById(long id) {
    return doctorDao.getDoctorById(id);
  }

  @Override
  public List<Doctor> getDoctorsByCity(String city) {
    return doctorDao.getDoctorsByCity(city);
  }

  @Override
  public List<Doctor> getDoctorsByHealthInsurance(String healthInsurance) {
    return doctorDao.getDoctorsByHealthInsurance(healthInsurance);
  }

  @Override
  public List<Doctor> getDoctorsBySpecialty(String specialty) {
    return doctorDao.getDoctorsBySpecialty(specialty);
  }

  @Override
  public List<Doctor> getDoctors() {
    return doctorDao.getDoctors();
  }
}
