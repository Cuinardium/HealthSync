package ar.edu.itba.paw.interfaces.persistence;

import java.util.Optional;

import ar.edu.itba.paw.models.Patient;

public interface PatientDao {

  public long createPatient(long patientId);

  public void addHealthInsurance(long userId, int healthInsuranceCode);

  public Optional<Patient> getPatientById(long id); 
}

