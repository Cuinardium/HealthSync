package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DoctorService {

  // =============== Inserts ===============
  
  public Doctor createDoctor(
      String email,
      String password,
      String firstName,
      String lastName,
      Specialty specialty,
      City city,
      String address,
      List<HealthInsurance> healthInsurances,
      AttendingHours attendingHours);

  // =============== Updates ===============

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
      Image image);

  // =============== Queries ===============

  public Optional<Doctor> getDoctorById(long id);

  public Page<Doctor> getFilteredDoctors(
      String name,
      Specialty specialty,
      City city,
      HealthInsurance healthInsurance,
      int page,
      int pageSize);

  public List<Doctor> getDoctors();

  // Get all Specialties and health insurances that are used by doctors
  public Map<Specialty, Integer> getUsedSpecialties();

  public Map<HealthInsurance, Integer> getUsedHealthInsurances();

  // Gets all cities used by doctors & qty of appearences
  public Map<City, Integer> getUsedCities();
}
