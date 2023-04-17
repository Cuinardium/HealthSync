package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Doctor;
import java.util.Optional;

public interface DoctorDao {
  public long createDoctor(long userId, long specialtyId);

  public Optional<Doctor> getDoctorById(long id);

  public void addLocation(long doctorId, long locationId);

  public void addHealthInsurance(long doctorId, long healthInsuranceId);
}
