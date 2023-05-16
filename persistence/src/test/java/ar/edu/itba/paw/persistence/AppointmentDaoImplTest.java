package ar.edu.itba.paw.persistence;

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

import ar.edu.itba.paw.persistence.config.TestConfig;

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
    // TODO: implement test
  }


  @Test
  public void testGetAppointmentById() {
    // TODO: implement test
  }

  @Test public void testGetAppointment() {};

  @Test public void testGetAppointmentsForPatient() {};

  @Test public void testGetAppointmentsForDoctor() {};

  @Test public void testGetFilteredAppointmentsForPatient() {};

  @Test public void testGetFilteredAppointmentsForDoctor() {};

  void updateAppointmentStatus() {};

}


