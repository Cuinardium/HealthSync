package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Patient;
import java.util.Optional;

public interface PatientDao {

  public long createPatient(long patientId);

  public void addHealthInsurance(long userId, int healthInsuranceCode);

  public void updatePatientInfo(long patientId, int healthInsuranceCode);

  public Optional<Patient> getPatientById(long id);
}
