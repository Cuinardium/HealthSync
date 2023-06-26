package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.VacationCollisionException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.VacationNotFoundException;
import ar.edu.itba.paw.models.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DoctorDao {

  // =============== Inserts ===============

  public Doctor createDoctor(Doctor doctor) throws DoctorAlreadyExistsException;

  // =============== Updates ===============

  public Doctor updateDoctorInfo(
      long doctorId,
      Specialty specialty,
      String city,
      String address,
      Set<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours)
      throws DoctorNotFoundException;

  public Doctor addVacation(long doctorId, Vacation vacation)
      throws DoctorNotFoundException, VacationCollisionException;

  public Doctor removeVacation(long doctorId, Vacation vacation)
      throws DoctorNotFoundException, VacationNotFoundException;

  public void deleteOldVacations(LocalDate today, ThirtyMinuteBlock now);

  // =============== Queries ===============

  public Optional<Doctor> getDoctorById(long id);

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
      Integer pageSize);

  public List<Doctor> getDoctors();

  // Get used specialties and health insurances
  public Map<HealthInsurance, Integer> getUsedHealthInsurances();

  public Map<Specialty, Integer> getUsedSpecialties();

  // Get all city present in the database & qty of appearences
  public Map<String, Integer> getUsedCities();

  public List<Specialty> getPopularSpecialties();
}
