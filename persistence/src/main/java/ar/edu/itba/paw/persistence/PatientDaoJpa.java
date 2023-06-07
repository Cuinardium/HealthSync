package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Patient;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import org.springframework.stereotype.Repository;

@Repository
public class PatientDaoJpa implements PatientDao {

  @PersistenceContext private EntityManager em;

  @Override
  public Patient createPatient(Patient patient)
      throws PatientAlreadyExistsException, EmailAlreadyExistsException {
    if (patient.getId() != null && getPatientById(patient.getId()).isPresent()) {
      throw new PatientAlreadyExistsException();
    }

    try {
      em.persist(patient);
      return patient;
    } catch (PersistenceException e) {
      throw new EmailAlreadyExistsException();
    }
  }

  @Override
  public Patient updatePatientInfo(long patientId, HealthInsurance healthInsurance)
      throws PatientNotFoundException, EmailAlreadyExistsException {
    Patient patient = getPatientById(patientId).orElseThrow(PatientNotFoundException::new);
    patient.setHealthInsurance(healthInsurance);

    try {
      em.persist(patient);
      return patient;
    } catch (PersistenceException e) {
      throw new EmailAlreadyExistsException();
    }
  }

  @Override
  public Optional<Patient> getPatientById(long patientId) {
    return Optional.ofNullable(em.find(Patient.class, patientId));
  }
}
