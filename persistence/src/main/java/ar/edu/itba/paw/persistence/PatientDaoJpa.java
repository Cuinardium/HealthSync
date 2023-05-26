package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Patient;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class PatientDaoJpa implements PatientDao {

  @PersistenceContext private EntityManager em;

  @Override
  public Patient createPatient(Patient patient)
      throws PatientAlreadyExistsException, IllegalStateException {
    if (patient.getId() != null && getPatientById(patient.getId()).isPresent()) {
      throw new PatientAlreadyExistsException();
    }

    em.persist(patient);

    return patient;
  }

  @Override
  public Patient updatePatientInfo(long patientId, HealthInsurance healthInsurance)
      throws PatientNotFoundException {
    Patient patient = getPatientById(patientId).orElseThrow(PatientNotFoundException::new);
    patient.setHealthInsurance(healthInsurance);

    em.persist(patient);

    return patient;
  }

  @Override
  public Optional<Patient> getPatientById(long patientId) {
    return Optional.ofNullable(em.find(Patient.class, patientId));
  }
}
