package ar.edu.itba.paw.interfaces.services;

import java.util.List;
import java.util.Optional;

import ar.edu.itba.paw.models.Doctor;

public interface DoctorService {
  public Doctor createDoctor(
      String email,
      String password,
      String firstName,
      String lastName,
      int healthInsuranceCode,
      int specialtyCode,
      int cityCode,
      String address);

  public Optional<Doctor> getDoctorById(long id);

  public List<Doctor> getFilteredDoctors(String name, int specialtyCode, int cityCode, int healthInsuranceCode);
  
  public List<Doctor> getDoctors();
}
