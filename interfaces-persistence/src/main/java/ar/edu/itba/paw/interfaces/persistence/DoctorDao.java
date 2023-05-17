package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.models.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DoctorDao {

  // =============== Inserts ===============

  Doctor createDoctor(
      long userId,
      Specialty specialty,
      City city,
      String address,
      List<HealthInsurance> healthInsurances,
      AttendingHours attendingHours)
      throws DoctorAlreadyExistsException, IllegalStateException;

  // =============== Updates ===============

  Doctor updateDoctorInfo(
      long doctorId,
      Specialty specialty,
      City city,
      String address,
      List<HealthInsurance> healthInsurances,
      AttendingHours attendingHours)
      throws DoctorNotFoundException;

  // =============== Queries ===============

  Optional<Doctor> getDoctorById(long id);

  Page<Doctor> getFilteredDoctors(
      String name,
      LocalDate date,
      ThirtyMinuteBlock fromTime,
      ThirtyMinuteBlock toTime,
      Specialty specialty,
      City city,
      HealthInsurance healthInsurance,
      Integer page,
      Integer pageSize);

  List<Doctor> getDoctors();

  // Get used specialties and health insurances
  Map<HealthInsurance, Integer> getUsedHealthInsurances();

  Map<Specialty, Integer> getUsedSpecialties();

  // Get all city present in the database & qty of appearences
  Map<City, Integer> getUsedCities();
}
