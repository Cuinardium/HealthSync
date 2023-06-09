package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.models.*;
import java.time.LocalDate;
import java.util.List;
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
      City city,
      String address,
      Set<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours)
      throws EmailInUseException;

  public Review addReview(long doctorId, Review review) throws DoctorNotFoundException;

  // =============== Updates ===============

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
      throws DoctorNotFoundException, EmailInUseException;

  public Doctor updateReviews(long doctorId, List<Review> reviews) throws DoctorNotFoundException;

  // =============== Queries ===============

  public Optional<Doctor> getDoctorById(long id);

  public Page<Doctor> getFilteredDoctors(
      String name,
      LocalDate date,
      ThirtyMinuteBlock fromTime,
      ThirtyMinuteBlock toTime,
      Specialty specialty,
      City city,
      HealthInsurance healthInsurance,
      Integer page,
      Integer pageSize);

  public List<Doctor> getDoctors();

  // Get all Specialties and health insurances that are used by doctors
  public Map<Specialty, Integer> getUsedSpecialties();

  public Map<HealthInsurance, Integer> getUsedHealthInsurances();

  // Gets all cities used by doctors & qty of appearences
  public Map<City, Integer> getUsedCities();

  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize);
}
