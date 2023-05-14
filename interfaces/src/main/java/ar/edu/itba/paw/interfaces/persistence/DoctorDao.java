package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DoctorDao {
  public long createDoctor(long userId, int specialtyCode, AttendingHours attendingHours);

  public Optional<Doctor> getDoctorById(long id);

  public Page<Doctor> getFilteredDoctors(
      String name,
      int specialtyCode,
      int cityCode,
      int healthInsuranceCode,
      int page,
      int pageSize);

  public List<Doctor> getDoctors();

  public void addLocation(long doctorId, long locationId);

  public void addHealthInsurance(long doctorId, int healthInsuranceCode);

  public void updateInformation(
      long doctorId, List<Integer> healthInsuranceCodes, int specialtyCode, int cityCode, String address);

  public void updateAttendingHours(long doctorId, AttendingHours attendingHours);

  // Get used specialties and health insurances
  public Map<Integer, Integer> getUsedHealthInsurances();

  public Map<Integer, Integer> getUsedSpecialties();
}
