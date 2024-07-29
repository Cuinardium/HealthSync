package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.persistence.config.TestConfig;
import java.util.Locale;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class PatientDaoImplTest {
  private static final Long INSERTED_PATIENT_ID = 5L;
  private static final String INSERTED_PATIENT_EMAIL = "patient@email.com";
  private static final String INSERTED_PATIENT_PASSWORD = "patient_password";
  private static final String INSERTED_PATIENT_FIRST_NAME = "patient_first_name";
  private static final String INSERTED_PATIENT_LAST_NAME = "patient_last_name";
  private static final Image INSERTED_PATIENT_IMAGE = null;
  private static final Locale INSERTED_PATIENT_LOCALE = new Locale("en");
  private static final HealthInsurance INSERTED_PATIENT_HEALTH_INSURANCE = HealthInsurance.OMINT;

  private static final Long AUX_PATIENT_ID = 2L;
  private static final String AUX_PATIENT_EMAIL = "notpatient_1@email.com";
  private static final String AUX_PATIENT_PASSWORD = "notpatient_password";
  private static final String AUX_PATIENT_FIRST_NAME = "notpatient_first_name";
  private static final String AUX_PATIENT_LAST_NAME = "notpatient_last_name";
  private static final Image AUX_PATIENT_IMAGE = new Image.Builder(null).id(2L).build();
  private static final HealthInsurance AUX_PATIENT_HEALTH_INSURANCE = HealthInsurance.OSDE;
  private static final Locale AUX_PATIENT_LOCALE = new Locale("es");

  private static final Patient PATIENT_5 =
      new Patient.Builder(
              INSERTED_PATIENT_EMAIL,
              INSERTED_PATIENT_PASSWORD,
              INSERTED_PATIENT_FIRST_NAME,
              INSERTED_PATIENT_LAST_NAME,
              INSERTED_PATIENT_HEALTH_INSURANCE,
              INSERTED_PATIENT_LOCALE)
          .id(INSERTED_PATIENT_ID)
          .image(INSERTED_PATIENT_IMAGE)
          .isVerified(true)
          .build();

  private static final Patient AUX_PATIENT =
      new Patient.Builder(
              AUX_PATIENT_EMAIL,
              AUX_PATIENT_PASSWORD,
              AUX_PATIENT_FIRST_NAME,
              AUX_PATIENT_LAST_NAME,
              AUX_PATIENT_HEALTH_INSURANCE,
              AUX_PATIENT_LOCALE)
          .image(AUX_PATIENT_IMAGE)
          .isVerified(false)
          .build();

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @PersistenceContext private EntityManager em;

  @Autowired private PatientDaoJpa patientDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  // TODO: ADDRESS COLLITION ON EMAIL
  @Test
  public void testCreatePatient() throws PatientAlreadyExistsException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Patient patient = patientDao.createPatient(AUX_PATIENT);
    // 3. Meaningful assertions

    em.flush();

    // No lo testeamos porque podria ser 1 o 2 depende el orden de los test
    // Assert.assertEquals(AUX_PATIENT_ID, patient.getId());
    Assert.assertEquals(AUX_PATIENT_EMAIL, patient.getEmail());
    Assert.assertEquals(AUX_PATIENT_PASSWORD, patient.getPassword());
    Assert.assertEquals(AUX_PATIENT_FIRST_NAME, patient.getFirstName());
    Assert.assertEquals(AUX_PATIENT_LAST_NAME, patient.getLastName());
    Assert.assertEquals(AUX_PATIENT_IMAGE, patient.getImage());
    Assert.assertEquals(AUX_PATIENT_HEALTH_INSURANCE, patient.getHealthInsurance());
    Assert.assertEquals(AUX_PATIENT_LOCALE, patient.getLocale());

    Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "patient"));
  }

  @Test
  public void testCreatePatientAlreadyExists() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(PatientAlreadyExistsException.class, () -> patientDao.createPatient(PATIENT_5));
    // 3. Meaningful assertions
  }

  @Test
  public void testUpdatePatientInfo() throws PatientNotFoundException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Patient patient =
        patientDao.updatePatientInfo(INSERTED_PATIENT_ID, AUX_PATIENT_HEALTH_INSURANCE);

    // 3. Meaningful assertions

    Assert.assertEquals(INSERTED_PATIENT_ID, patient.getId());
    Assert.assertEquals(INSERTED_PATIENT_EMAIL, patient.getEmail());
    Assert.assertEquals(INSERTED_PATIENT_PASSWORD, patient.getPassword());
    Assert.assertEquals(INSERTED_PATIENT_FIRST_NAME, patient.getFirstName());
    Assert.assertEquals(INSERTED_PATIENT_LAST_NAME, patient.getLastName());
    Assert.assertEquals(INSERTED_PATIENT_IMAGE, patient.getImage());
    Assert.assertEquals(AUX_PATIENT_HEALTH_INSURANCE, patient.getHealthInsurance());
    Assert.assertEquals(INSERTED_PATIENT_LOCALE, patient.getLocale());
  }

  @Test
  public void testUpdatePatientInfoPatientDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        PatientNotFoundException.class,
        () -> patientDao.updatePatientInfo(AUX_PATIENT_ID, INSERTED_PATIENT_HEALTH_INSURANCE));
    // 3. Meaningful assertions
  }

  @Test
  public void testGetPatientById() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<Patient> maybePatient = patientDao.getPatientById(INSERTED_PATIENT_ID);
    // 3. Meaningful assertions
    Assert.assertTrue(maybePatient.isPresent());
    Assert.assertEquals(PATIENT_5, maybePatient.get());
  }

  @Test
  public void testGetPatientByIdDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<Patient> maybePatient = patientDao.getPatientById(AUX_PATIENT_ID);
    // 3. Meaningful assertions
    Assert.assertFalse(maybePatient.isPresent());
  }
}
