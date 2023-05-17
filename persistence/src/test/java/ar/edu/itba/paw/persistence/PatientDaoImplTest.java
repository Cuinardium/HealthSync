package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.PatientNotFoundException;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.persistence.config.TestConfig;
import java.util.Optional;
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
  private static final long INSERTED_PATIENT_ID = 1;
  private static final String INSERTED_PATIENT_EMAIL = "patient@email.com";
  private static final String INSERTED_PATIENT_PASSWORD = "patient_password";
  private static final String INSERTED_PATIENT_FIRST_NAME = "patient_first_name";
  private static final String INSERTED_PATIENT_LAST_NAME = "patient_last_name";
  private static final Long INSERTED_PATIENT_PFP_ID = null;
  private static final HealthInsurance INSERTED_PATIENT_HEALTH_INSURANCE = HealthInsurance.OMINT;

  private static final long AUX_PATIENT_ID = 2L;
  private static final String AUX_PATIENT_EMAIL = "notpatient@email.com";
  private static final String AUX_PATIENT_PASSWORD = "notpatient_password";
  private static final String AUX_PATIENT_FIRST_NAME = "notpatient_first_name";
  private static final String AUX_PATIENT_LAST_NAME = "notpatient_last_name";
  private static final Long AUX_PATIENT_PFP_ID = 2L;
  private static final HealthInsurance AUX_PATIENT_HEALTH_INSURANCE = HealthInsurance.OSDE;

  private static final Patient PATIENT_1 =
      new Patient(
          INSERTED_PATIENT_ID,
          INSERTED_PATIENT_EMAIL,
          INSERTED_PATIENT_PASSWORD,
          INSERTED_PATIENT_FIRST_NAME,
          INSERTED_PATIENT_LAST_NAME,
          INSERTED_PATIENT_PFP_ID,
          INSERTED_PATIENT_HEALTH_INSURANCE);

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private PatientDaoImpl patientDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  @Test
  public void testCreatePatient() throws PatientAlreadyExistsException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Patient patient = patientDao.createPatient(AUX_PATIENT_ID, AUX_PATIENT_HEALTH_INSURANCE);
    // 3. Meaningful assertions

    Assert.assertEquals(AUX_PATIENT_ID, patient.getId());
    Assert.assertEquals(AUX_PATIENT_EMAIL, patient.getEmail());
    Assert.assertEquals(AUX_PATIENT_PASSWORD, patient.getPassword());
    Assert.assertEquals(AUX_PATIENT_FIRST_NAME, patient.getFirstName());
    Assert.assertEquals(AUX_PATIENT_LAST_NAME, patient.getLastName());
    Assert.assertEquals(INSERTED_PATIENT_PFP_ID, patient.getProfilePictureId());
    Assert.assertEquals(AUX_PATIENT_HEALTH_INSURANCE, patient.getHealthInsurance());

    Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "patient"));
  }

  @Test
  public void testCreatePatientAlreadyExists() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        PatientAlreadyExistsException.class,
        () -> patientDao.createPatient(INSERTED_PATIENT_ID, AUX_PATIENT_HEALTH_INSURANCE));
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
    Assert.assertEquals(INSERTED_PATIENT_PFP_ID, patient.getProfilePictureId());
    Assert.assertEquals(AUX_PATIENT_HEALTH_INSURANCE, patient.getHealthInsurance());
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
    Assert.assertEquals(PATIENT_1, maybePatient.get());
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
