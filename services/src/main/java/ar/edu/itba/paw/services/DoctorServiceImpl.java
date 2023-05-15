package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.services.DoctorService;
import ar.edu.itba.paw.interfaces.services.LocationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorServiceImpl implements DoctorService {

  private final DoctorDao doctorDao;

  private final UserService userService;
  private final LocationService locationService;

  @Autowired
  public DoctorServiceImpl(
      DoctorDao doctorDao, UserService userService, LocationService locationService) {

    this.doctorDao = doctorDao;

    this.userService = userService;
    this.locationService = locationService;
  }

  @Transactional
  @Override
  public Doctor createDoctor(
      String email,
      String password,
      String firstName,
      String lastName,
      List<Integer> healthInsuranceCodes,
      int specialtyCode,
      int cityCode,
      String address,
      AttendingHours attendingHours) {

    // Create user
    User user = userService.createUser(email, password, firstName, lastName);
    long userId = user.getId();
    long pfpId = user.getProfilePictureId();

    // Create Location
    long locationId = locationService.createLocation(cityCode, address);

    // Create doctor
    long doctorId = doctorDao.createDoctor(userId, specialtyCode, attendingHours);

    // Add location to doctor
    doctorDao.addLocation(doctorId, locationId);

    // Add health insurances to doctor
    for (int healthInsuranceCode : healthInsuranceCodes) {
      doctorDao.addHealthInsurance(doctorId, healthInsuranceCode);
    }

    // Enums
    List<HealthInsurance> healthInsurances =
        healthInsuranceCodes
            .stream()
            .map(HealthInsurance::getHealthInsurance)
            .collect(Collectors.toList());

    Specialty specialty = Specialty.values()[specialtyCode];
    Location location = new Location(locationId, City.values()[cityCode], address);

    return new Doctor(
        doctorId,
        email,
        password,
        firstName,
        lastName,
        pfpId,
        healthInsurances,
        specialty,
        location,
        attendingHours);
  }

  @Transactional
  @Override
  public void updateInformation(
      long doctorId,
      String email,
      String firstName,
      String lastName,
      List<Integer> healthInsuranceCodes,
      int specialtyCode,
      int cityCode,
      String address,
      Image image) {
    userService.editUser(doctorId, email, firstName, lastName, image);
    doctorDao.updateInformation(doctorId, healthInsuranceCodes, specialtyCode, cityCode, address);
  }

  @Transactional
  @Override
  public void updateAttendingHours(long doctorId, AttendingHours attendingHours) {
    doctorDao.updateAttendingHours(doctorId, attendingHours);
  }

  @Override
  public Optional<Doctor> getDoctorById(long id) {
    return doctorDao.getDoctorById(id);
  }

  @Override
  public Page<Doctor> getFilteredDoctors(
      String name,
      int specialtyCode,
      int cityCode,
      int healthInsuranceCode,
      int page,
      int pageSize) {
    return doctorDao.getFilteredDoctors(
        name, specialtyCode, cityCode, healthInsuranceCode, page, pageSize);
  }

  @Override
  public List<Doctor> getDoctors() {
    return doctorDao.getDoctors();
  }

  // Get all Specialties and health insurances that are used by doctors
  @Override
  public Map<HealthInsurance, Integer> getUsedHealthInsurances() {

    // Get all health insurances codes present in the database
    Map<Integer, Integer> healthInsurancesCodes = doctorDao.getUsedHealthInsurances();

    Map<HealthInsurance, Integer> healthInsurancesMap = new HashMap<>();
    healthInsurancesCodes.forEach(
        (key, value) -> healthInsurancesMap.put(HealthInsurance.getHealthInsurance(key), value));

    return healthInsurancesMap;
  }

  @Override
  public Map<Specialty, Integer> getUsedSpecialties() {

    // Get all specialties codes present in the database & qty of appearences
    Map<Integer, Integer> specialtiesCodes = doctorDao.getUsedSpecialties();

    Map<Specialty, Integer> specialtyMap = new HashMap<>();
    specialtiesCodes.forEach((key, value) -> specialtyMap.put(Specialty.getSpecialty(key), value));

    // Return a list of specialties using the codes
    return specialtyMap;
  }
}
