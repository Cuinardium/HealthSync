package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import java.util.Optional;

public interface PatientService {

  // =============== Inserts ===============

  public Patient createPatient(
      String email,
      String password,
      String firstName,
      String lastName,
      HealthInsurance healthInsurance)
      throws EmailInUseException, IllegalStateException;

  // =============== Updates ===============

  public Patient updatePatient(
      long patientId,
      String email,
      String firstName,
      String lastName,
      HealthInsurance healthInsurance,
      Image image);

  // =============== Queries ===============

  public Optional<Patient> getPatientById(long id);
}
