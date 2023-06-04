package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.models.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface DoctorDao {

  // =============== Inserts ===============

  Doctor createDoctor(Doctor doctor) throws DoctorAlreadyExistsException, IllegalStateException;

  Review addReview(long doctorId, Review review) throws DoctorNotFoundException;

  // =============== Updates ===============

  Doctor updateDoctorInfo(
      long doctorId,
      Specialty specialty,
      City city,
      String address,
      List<HealthInsurance> healthInsurances,
      Set<AttendingHours> attendingHours,
      List<Review> reviews)
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

  Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize);
}
