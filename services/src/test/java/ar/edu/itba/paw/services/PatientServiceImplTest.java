package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {

  private static final long ID = 0;
  private static final String EMAIL = "email";
  private static final String PASSWORD = "password";
  private static final String FIRST_NAME = "first_name";
  private static final String LAST_NAME = "last_name";
  private static final HealthInsurance HEALTH_INSURANCE = HealthInsurance.NONE;
  private static final Long PFP_ID = null;

  private static final User USER = new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, PFP_ID);
  private static final Patient PATIENT =
      new Patient(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, PFP_ID, HEALTH_INSURANCE);
  private static final String EMAIL_NEW = "new_email";
  private static final String FIRST_NAME_NEW = "new_fist_name";
  private static final String LAST_NAME_NEW = "new_last_name";
  private static final Image IMAGE = new Image(new byte[0]);
  private static final HealthInsurance HEALTH_INSURANCE_NEW = HealthInsurance.OMINT;
  private static final Patient PATIENT_UPDATED =
      new Patient(
          ID, EMAIL_NEW, PASSWORD, FIRST_NAME_NEW, LAST_NAME_NEW, PFP_ID, HEALTH_INSURANCE_NEW);

  @Mock private PatientDao patientDao;
  @Mock private UserService userService;

  @InjectMocks private PatientServiceImpl ps;

  @Test
  public void testCreatePatient()
      throws IllegalStateException, PatientAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(userService.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME)).thenReturn(USER);
    Mockito.when(patientDao.createPatient(ID, HEALTH_INSURANCE)).thenReturn(PATIENT);
    // 2. Ejercitar la class under test
    Patient patient = ps.createPatient(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE);
    // 3. Meaningful assertions
    Assert.assertEquals(PATIENT, patient);
  }

  @Test(expected = IllegalStateException.class)
  public void testCreatePatientAlreadyExists()
      throws IllegalStateException, PatientAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(userService.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME)).thenReturn(USER);
    Mockito.when(patientDao.createPatient(ID, HEALTH_INSURANCE))
        .thenThrow(PatientAlreadyExistsException.class);
    // 2. Ejercitar la class under test
    ps.createPatient(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE);
    // 3. Meaningful assertions
  }

  @Test(expected = EmailInUseException.class)
  public void testCreatePatientUserAlreadyExists()
      throws IllegalStateException, PatientAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(userService.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME))
        .thenThrow(EmailInUseException.class);
    // 2. Ejercitar la class under test
    ps.createPatient(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE);
    // 3. Meaningful assertions
  }

  @Test
  public void testUpdatePatient() throws PatientNotFoundException, UserNotFoundException {
    // 1. Precondiciones
    Mockito.when(patientDao.updatePatientInfo(ID, HEALTH_INSURANCE_NEW))
        .thenReturn(PATIENT_UPDATED);
    // 2. Ejercitar la class under test
    Patient patient =
        ps.updatePatient(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, HEALTH_INSURANCE_NEW, IMAGE);
    // 3. Meaningful assertions
    Assert.assertEquals(PATIENT_UPDATED, patient);
  }

  // TODO: cng excep
  @Test(expected = RuntimeException.class)
  public void testUpdatePatientDoesNotExist()
      throws PatientNotFoundException, UserNotFoundException {
    // 1. Precondiciones
    Mockito.when(patientDao.updatePatientInfo(ID, HEALTH_INSURANCE_NEW))
        .thenThrow(PatientNotFoundException.class);
    // 2. Ejercitar la class under test
    ps.updatePatient(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, HEALTH_INSURANCE_NEW, IMAGE);
    // 3. Meaningful assertions
  }

  @Test
  public void testGetPatientById() {
    // 1. Precondiciones
    Mockito.when(patientDao.getPatientById(ID)).thenReturn(Optional.of(PATIENT));
    // 2. Ejercitar la class under test
    Optional<Patient> patient = ps.getPatientById(ID);
    // 3. Meaningful assertions
    Assert.assertTrue(patient.isPresent());
    Assert.assertEquals(PATIENT, patient.get());
  }

  @Test
  public void testGetPatientByIdDoesNotExist() {
    // 1. Precondiciones
    Mockito.when(patientDao.getPatientById(ID)).thenReturn(Optional.empty());
    // 2. Ejercitar la class under test
    Optional<Patient> patient = ps.getPatientById(ID);
    // 3. Meaningful assertions
    Assert.assertFalse(patient.isPresent());
  }
}
