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

  @Transactional
  @Override
  public Patient createPatient(
      String email, String password, String firstName, String lastName, int healthInsuranceCode) {

    // Create User
    User user = userService.createUser(email, password, firstName, lastName);

    // Create Patient
    long patientId = patientDao.createPatient(user.getId());

    // Add Health Insurance
    patientDao.addHealthInsurance(patientId, healthInsuranceCode);

    return new Patient(
        patientId,
        email,
        password,
        firstName,
        lastName,
        user.getProfilePictureId(),
        HealthInsurance.values()[healthInsuranceCode]);
  }

  @Transactional
  @Override
  public void updateInformation(
      long patientId,
      String email,
      String firstName,
      String lastName,
      int healthInsuranceCode,
      Image image) {
    userService.editUser(patientId, email, firstName, lastName, image);
    patientDao.updatePatientInfo(patientId, healthInsuranceCode);
  }

  @Override
  public Optional<Patient> getPatientById(long id) {
    return patientDao.getPatientById(id);
  }
}
