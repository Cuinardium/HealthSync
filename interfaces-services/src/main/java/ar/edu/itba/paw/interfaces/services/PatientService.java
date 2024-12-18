package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import java.util.Locale;
import java.util.Optional;

public interface PatientService {

  // =============== Inserts ===============

  public Patient createPatient(
      String email,
      String password,
      String firstName,
      String lastName,
      HealthInsurance healthInsurance,
      Locale locale)
      throws EmailInUseException;

  // =============== Updates ===============

  public Patient updatePatient(
      long patientId,
      String email,
      String firstName,
      String lastName,
      HealthInsurance healthInsurance,
      Image image,
      Locale locale)
      throws EmailInUseException, PatientNotFoundException;

  // =============== Queries ===============

  public Optional<Patient> getPatientById(long id);
}
