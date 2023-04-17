package ar.edu.itba.paw.interfaces.services;

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
}
