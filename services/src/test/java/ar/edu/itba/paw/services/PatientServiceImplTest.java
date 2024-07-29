package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.interfaces.services.MailService;
import ar.edu.itba.paw.interfaces.services.TokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;

import java.util.Locale;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class PatientServiceImplTest {

  private static final long ID = 0;
  private static final String EMAIL = "email";
  private static final String PASSWORD = "password";
  private static final String PASSWORD_ENCODED = "password_encoded";
  private static final String FIRST_NAME = "first_name";
  private static final String LAST_NAME = "last_name";
  private static final HealthInsurance HEALTH_INSURANCE = HealthInsurance.NONE;
  private static final Image IMAGE = null;
  private static final Locale LOCALE = new Locale("en");

  private static final User USER =
      new User.Builder(
              EMAIL,
              PASSWORD,
              FIRST_NAME,
              LAST_NAME,
              LOCALE)
              .id(ID)
              .isVerified(true)
              .image(IMAGE)
              .build();
  private static final Patient PATIENT =
      new Patient.Builder(
              EMAIL,
              PASSWORD_ENCODED,
              FIRST_NAME,
              LAST_NAME,
              HEALTH_INSURANCE,
              LOCALE)
              .id(ID)
              .isVerified(true)
              .image(IMAGE)
              .build();
  private static final String EMAIL_NEW = "new_email";
  private static final String FIRST_NAME_NEW = "new_fist_name";
  private static final String LAST_NAME_NEW = "new_last_name";
  private static final HealthInsurance HEALTH_INSURANCE_NEW = HealthInsurance.OMINT;
  private static final Patient PATIENT_UPDATED =
        new Patient.Builder(
                EMAIL_NEW,
                PASSWORD,
                FIRST_NAME_NEW,
                LAST_NAME_NEW,
                HEALTH_INSURANCE_NEW,
                LOCALE)
                .id(ID)
                .isVerified(true)
                .image(IMAGE)
                .build();
  private static final Locale LOCALE_NEW = new Locale("es");

  @Mock private PatientDao patientDao;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserService userService;
  @Mock private MailService mailService;
  @Mock private TokenService tokenService;

  @InjectMocks private PatientServiceImpl ps;

  // =========================== Create patient ===========================

  @Test
  public void testCreatePatient() throws PatientAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(userService.getUserByEmail(Mockito.eq(EMAIL))).thenReturn(Optional.empty());
    Mockito.when(userService.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(USER));

    Mockito.when(
            patientDao.createPatient(
                new Patient.Builder(
                        EMAIL, PASSWORD_ENCODED, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE, LOCALE)
                    .build()))
        .thenReturn(PATIENT);

    Mockito.when(tokenService.createToken(Mockito.any(User.class)))
        .thenReturn(Mockito.mock(VerificationToken.class));
    Mockito.doNothing()
        .when(mailService)
        .sendConfirmationMail(Mockito.any(VerificationToken.class));

    // 2. Ejercitar la class under test
    Patient patient =
        ps.createPatient(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE, LOCALE);
    // 3. Meaningful assertions
    Assert.assertEquals(PATIENT, patient);
  }

  // TODO: make a more specific exception
  @Test(expected = IllegalStateException.class)
  public void testCreatePatientAlreadyExists()
      throws PatientAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(userService.getUserByEmail(Mockito.eq(EMAIL))).thenReturn(Optional.empty());

    Mockito.when(
            patientDao.createPatient(
                new Patient.Builder(
                        EMAIL, PASSWORD_ENCODED, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE, LOCALE)
                    .build()))
        .thenThrow(PatientAlreadyExistsException.class);

    // 2. Ejercitar la class under test
    ps.createPatient(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE, LOCALE);
    // 3. Meaningful assertions
  }

  @Test(expected = EmailInUseException.class)
  public void testCreatePatientEmailAlreadyExists()
      throws PatientAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(userService.getUserByEmail(Mockito.eq(EMAIL))).thenReturn(Optional.of(USER));

    // 2. Ejercitar la class under test
    ps.createPatient(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE, LOCALE);
    // 3. Meaningful assertions
  }

  // =========================== Update patient ===========================

  @Test
  public void testUpdatePatient()
      throws PatientNotFoundException, UserNotFoundException, EmailInUseException,
          ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException {
    // 1. Precondiciones
    Mockito.when(patientDao.updatePatientInfo(ID, HEALTH_INSURANCE_NEW))
        .thenReturn(PATIENT_UPDATED);
    // 2. Ejercitar la class under test
    Patient patient =
        ps.updatePatient(
            ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, HEALTH_INSURANCE_NEW, IMAGE, LOCALE_NEW);
    // 3. Meaningful assertions
    Assert.assertEquals(PATIENT_UPDATED, patient);
  }

  @Test(expected = EmailInUseException.class)
  public void testUpdatePatientDuplicateEmail()
      throws PatientNotFoundException, UserNotFoundException, EmailInUseException,
          ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException {

    // 1. Precondiciones
    Mockito.when(
            userService.updateUser(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE, LOCALE_NEW))
        .thenThrow(EmailInUseException.class);

    // 2. Ejercitar la class under test
    ps.updatePatient(
        ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, HEALTH_INSURANCE_NEW, IMAGE, LOCALE_NEW);
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException.class)
  public void testUpdatePatientDoesNotExist()
      throws PatientNotFoundException, UserNotFoundException, EmailInUseException,
          ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException {
    // 1. Precondiciones
    Mockito.when(patientDao.updatePatientInfo(ID, HEALTH_INSURANCE_NEW))
        .thenThrow(PatientNotFoundException.class);
    // 2. Ejercitar la class under test
    ps.updatePatient(
        ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, HEALTH_INSURANCE_NEW, IMAGE, LOCALE_NEW);
    // 3. Meaningful assertions
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException.class)
  public void testUpdateUserDoesNotExist()
      throws PatientNotFoundException, UserNotFoundException, EmailInUseException,
          ar.edu.itba.paw.interfaces.services.exceptions.PatientNotFoundException {
    // 1. Precondiciones
    Mockito.when(
            userService.updateUser(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE, LOCALE_NEW))
        .thenThrow(UserNotFoundException.class);

    // 2. Ejercitar la class under test
    ps.updatePatient(
        ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, HEALTH_INSURANCE_NEW, IMAGE, LOCALE_NEW);
  }

  // =========================== Get patient by id ===========================

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
