package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Patient;
import java.util.Optional;

public interface PatientDao {

  // =============== Inserts ===============

  public Patient createPatient(long patientId, HealthInsurance healthInsurance);

  // =============== Updates ===============

  public Patient updatePatientInfo(long patientId, HealthInsurance healthInsurance);

  // =============== Queries ===============

  public Optional<Patient> getPatientById(long id);
}
