package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorDao {
  public long createDoctor(long userId, int specialtyCode, AttendingHours attendingHours);

  public Optional<Doctor> getDoctorById(long id);

  public List<Doctor> getFilteredDoctors(
      String name, int specialtyCode, int cityCode, int healthInsuranceCode);

  public List<Doctor> getDoctors();

  public void addLocation(long doctorId, long locationId);

  public void addHealthInsurance(long doctorId, int healthInsuranceCode);

  public void updateAttendingHours(long doctorId, AttendingHours attendingHours);
}
