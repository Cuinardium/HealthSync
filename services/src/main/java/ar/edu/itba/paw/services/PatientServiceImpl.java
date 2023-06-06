package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientServiceImpl implements PatientService {
  private final PatientDao patientDao;

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  @Autowired
  public PatientServiceImpl(
      PatientDao patientDao, UserService userService, PasswordEncoder passwordEncoder) {
    this.patientDao = patientDao;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  // =============== Inserts ===============

  @Transactional
  @Override
  public Patient createPatient(
      String email,
      String password,
      String firstName,
      String lastName,
      HealthInsurance healthInsurance)
      throws EmailInUseException {

    try {
      // TODO: revisar xq comente create user y era lo que tiraba el email in use excep
      // Create user
      // userService.createUser(email, password, firstName, lastName);
      // Create Patient
      Patient patient =
          patientDao.createPatient(
              new Patient(
                  null,
                  email,
                  passwordEncoder.encode(password),
                  firstName,
                  lastName,
                  null,
                  healthInsurance));
      return patient;
    } catch (PatientAlreadyExistsException e) {
      throw new IllegalStateException();
    }
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
      Image image)
      throws UserNotFoundException {

    try {
      userService.updateUser(patientId, email, firstName, lastName, image);
      return patientDao.updatePatientInfo(patientId, healthInsurance);
    } catch (PatientNotFoundException e) {
      throw new RuntimeException();
    }
  }

  // =============== Queries ===============

  @Transactional
  @Override
  public Optional<Patient> getPatientById(long id) {
    return patientDao.getPatientById(id);
  }
}
