package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorDao {
  public long createDoctor(long userId, long specialtyId);

  public Optional<Doctor> getDoctorById(long id);

  public List<Doctor> getFilteredDoctors(
      String name, String specialty, String city, String healthInsurance);

  public List<Doctor> getDoctors();

  public void addLocation(long doctorId, long locationId);

  public void addHealthInsurance(long doctorId, long healthInsuranceId);
}
