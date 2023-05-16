package ar.edu.itba.paw.persistence;

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

  private static final long AUX_PATIENT_ID = 3L;

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  @Autowired private PatientDaoImpl patientDao;

  @Test
  public void testCreatePatient() {};

  @Test
  public void testAddHealthInsurance() {};

  @Test
  public void testUpdateInformation() {};

  @Test
  public void testGetPatientById() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<Patient> maybePatient = patientDao.getPatientById(INSERTED_PATIENT_ID);
    // 3. Meaningful assertions
    Assert.assertTrue(maybePatient.isPresent());
    Assert.assertEquals(INSERTED_PATIENT_ID, maybePatient.get().getId());
    Assert.assertEquals(INSERTED_PATIENT_EMAIL, maybePatient.get().getEmail());
    Assert.assertEquals(INSERTED_PATIENT_PASSWORD, maybePatient.get().getPassword());
    Assert.assertEquals(INSERTED_PATIENT_FIRST_NAME, maybePatient.get().getFirstName());
    Assert.assertEquals(INSERTED_PATIENT_LAST_NAME, maybePatient.get().getLastName());
    Assert.assertEquals(INSERTED_PATIENT_PFP_ID, maybePatient.get().getProfilePictureId());
    Assert.assertEquals(INSERTED_PATIENT_HEALTH_INSURANCE, maybePatient.get().getHealthInsurance());
  };

  @Test
  public void testGetPatientByIdDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<Patient> maybePatient = patientDao.getPatientById(AUX_PATIENT_ID);
    // 3. Meaningful assertions
    Assert.assertFalse(maybePatient.isPresent());
  };
}
