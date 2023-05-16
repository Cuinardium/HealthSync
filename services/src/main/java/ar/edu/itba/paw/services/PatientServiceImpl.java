package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientServiceImpl implements PatientService {
  private final PatientDao patientDao;

  private final UserService userService;

  @Autowired
  public PatientServiceImpl(PatientDao patientDao, UserService userService) {
    this.patientDao = patientDao;
    this.userService = userService;
  }

  // =============== Inserts ===============

  @Transactional
  @Override
  public Patient createPatient(
      String email,
      String password,
      String firstName,
      String lastName,
      HealthInsurance healthInsurance) {

    // Create User
    User user = userService.createUser(email, password, firstName, lastName);

    // Create Patient
    Patient patient = patientDao.createPatient(user.getId(), healthInsurance);

    return patient;
  }

  // =============== Updates ===============

  @Transactional
  @Override
  public Patient updatePatient(
      long patientId,
      String email,
      String firstName,
      String lastName,
      HealthInsurance healthInsurance,
      Image image) {

    userService.updateUser(patientId, email, firstName, lastName, image);

    return patientDao.updatePatientInfo(patientId, healthInsurance);
  }

  // =============== Queries ===============

  @Override
  public Optional<Patient> getPatientById(long id) {
    return patientDao.getPatientById(id);
  }
}
