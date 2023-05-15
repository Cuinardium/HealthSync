package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import java.util.Optional;

public interface PatientService {

  public Patient createPatient(
      String email, String password, String firstName, String lastName, int healthInsuranceCode);

  public void updateInformation(
      long patientId,
      String email,
      String firstName,
      String lastName,
      int healthInsuranceCode,
      Image image);

  public Optional<Patient> getPatientById(long id);
}
