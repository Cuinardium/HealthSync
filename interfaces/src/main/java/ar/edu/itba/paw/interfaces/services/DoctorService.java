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
      String healthInsurance,
      String specialty,
      String city,
      String address);

  public Optional<Doctor> getDoctorById(long id);

  public List<Doctor> getDoctorsBySpecialty(String specialty);

  public List<Doctor> getDoctorsByCity(String city);

  public List<Doctor> getDoctorsByHealthInsurance(String healthInsurance);

  public List<Doctor> getDoctors();
}