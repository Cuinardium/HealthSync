package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Page;
import java.time.LocalDate;
import ar.edu.itba.paw.models.Specialty;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DoctorDao {

  // =============== Inserts ===============

  public Doctor createDoctor(
      long userId,
      Specialty specialty,
      City city,
      String address,
      List<HealthInsurance> healthInsurances,
      AttendingHours attendingHours);

  // =============== Updates ===============

  public Doctor updateDoctorInfo(
      long doctorId,
      Specialty specialty,
      City city,
      String address,
      List<HealthInsurance> healthInsurances,
      AttendingHours attendingHours);

  // =============== Queries ===============

  public Optional<Doctor> getDoctorById(long id);

  public Page<Doctor> getFilteredDoctors(
      String name,
      LocalDate date,
      Specialty specialty,
      City city,
      HealthInsurance healthInsurance,
      int page,
      int pageSize);

  public List<Doctor> getDoctors();

  // Get used specialties and health insurances
  public Map<HealthInsurance, Integer> getUsedHealthInsurances();

  public Map<Specialty, Integer> getUsedSpecialties();

  // Get all city present in the database & qty of appearences
  public Map<City, Integer> getUsedCities();
}
