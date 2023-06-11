package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import java.util.Locale;
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
      HealthInsurance healthInsurance,
      Locale locale)
      throws EmailInUseException {

    if (userService.getUserByEmail(email).isPresent()) {
      throw new EmailInUseException();
    }

    try {
      Patient patient =
          patientDao.createPatient(
              new Patient.Builder(
                      email,
                      passwordEncoder.encode(password),
                      firstName,
                      lastName,
                      healthInsurance,
                      locale)
                  .build());
      return patient;
    } catch (PatientAlreadyExistsException e) {
      throw new IllegalStateException("Patient should not exist when id is null");
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
      Image image,
      Locale locale)
      throws PatientNotFoundException, EmailInUseException {

    try {
      userService.updateUser(patientId, email, firstName, lastName, image, locale);
      return patientDao.updatePatientInfo(patientId, healthInsurance);
    } catch (UserNotFoundException
        | ar.edu.itba.paw.interfaces.persistence.exceptions.PatientNotFoundException e) {
      throw new PatientNotFoundException();
    }
  }

  // =============== Queries ===============

  @Transactional
  @Override
  public Optional<Patient> getPatientById(long id) {
    return patientDao.getPatientById(id);
  }
}
