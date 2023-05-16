package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.persistence.config.TestConfig;
import javax.sql.DataSource;
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
public class AppointmentDaoImplTest {

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private AppointmentDaoImpl appointmentDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  @Test
  public void testCreateAppointment() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // Appointment appointment = appointmentDao.createAppointment();
    // 3. Meaninful assertions
  }

  @Test
  public void testUpdateAppointment() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentById() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointment() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentsForPatient() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetAppointmentsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForPatient() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetFilteredAppointmentsForDoctor() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }
}
