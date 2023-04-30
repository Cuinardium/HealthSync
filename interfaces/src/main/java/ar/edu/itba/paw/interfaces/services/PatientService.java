package ar.edu.itba.paw.interfaces.services;

import java.util.Optional;

import ar.edu.itba.paw.models.Patient;

public interface PatientService {

  public Patient createPatient(
      String email, String password, String firstName, String lastName, int healthInsuranceCode);

  public Optional<Patient> getPatientById(long id); 
}
