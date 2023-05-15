package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Specialty;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DoctorService {
  public Doctor createDoctor(
      String email,
      String password,
      String firstName,
      String lastName,
      int healthInsuranceCode,
      int specialtyCode,
      int cityCode,
      String address,
      AttendingHours attendingHours);

  public void updateInformation(
      long doctorId,
      String email,
      String firstName,
      String lastName,
      int healthInsuranceCode,
      int specialtyCode,
      int cityCode,
      String address,
      Image image);

  public void updateAttendingHours(long doctorId, AttendingHours attendingHours);

  public Optional<Doctor> getDoctorById(long id);

  public List<Doctor> getFilteredDoctors(
      String name, int specialtyCode, int cityCode, int healthInsuranceCode);

  public List<Doctor> getDoctors();

  // Get all Specialties and health insurances that are used by doctors
  public Map<Specialty, Integer> getUsedSpecialties();

  public Map<HealthInsurance, Integer> getUsedHealthInsurances();
}
