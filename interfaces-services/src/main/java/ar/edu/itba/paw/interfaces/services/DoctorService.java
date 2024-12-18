package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.VacationInvalidException;
import ar.edu.itba.paw.interfaces.services.exceptions.VacationNotFoundException;
import ar.edu.itba.paw.models.*;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DoctorService {

  // =============== Inserts ===============

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
      throws EmailInUseException;

  // =============== Updates ===============

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
      throws DoctorNotFoundException, EmailInUseException;

  public Vacation addVacation(long doctorId, Vacation vacation)
      throws DoctorNotFoundException, VacationInvalidException;

  public Doctor removeVacation(long doctorId, Vacation vacation)
      throws DoctorNotFoundException, VacationNotFoundException;

  // =============== Queries ===============

  public Optional<Doctor> getDoctorById(long id);

  public Page<Doctor> getFilteredDoctors(
      String name,
      LocalDate date,
      ThirtyMinuteBlock fromTime,
      ThirtyMinuteBlock toTime,
      Set<Specialty> specialties,
      Set<String> cities,
      Set<HealthInsurance> healthInsurance,
      Integer minRating,
      Integer page,
      Integer pageSize);

  // Get all Specialties and health insurances that are used by doctors
  public Map<Specialty, Integer> getUsedSpecialties(
      String name,
      LocalDate date,
      ThirtyMinuteBlock fromTime,
      ThirtyMinuteBlock toTime,
      Set<Specialty> specialties,
      Set<String> cities,
      Set<HealthInsurance> healthInsurance,
      Integer minRating,
      Boolean sortByPopularity,
      Boolean reversed);

  public Map<HealthInsurance, Integer> getUsedHealthInsurances(
      String name,
      LocalDate date,
      ThirtyMinuteBlock fromTime,
      ThirtyMinuteBlock toTime,
      Set<Specialty> specialties,
      Set<String> cities,
      Set<HealthInsurance> healthInsurance,
      Integer minRating,
      Boolean sortByPopularity,
      Boolean reversed);

  // Gets all cities used by doctors & qty of appearences
  public Map<String, Integer> getUsedCities(
      String name,
      LocalDate date,
      ThirtyMinuteBlock fromTime,
      ThirtyMinuteBlock toTime,
      Set<Specialty> specialties,
      Set<String> cities,
      Set<HealthInsurance> healthInsurance,
      Integer minRating,
      Boolean sortByPopularity,
      Boolean reversed);

  // ================= Tasks =================

  // Deletes all vacations that have ended
  public void deleteOldVacations();
}
