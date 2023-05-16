package ar.edu.itba.paw.persistence;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.persistence.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DoctorDaoImplTest {

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private DoctorDaoImpl doctorDao;

  @Test public void testCreateDoctor() {};

  @Test public void testGetDoctorById() {};

  @Test public void testGetFilteredDoctors() {};

  @Test public void testGetDoctors() {};

  @Test public void testAddLocation() {};

  @Test public void testAddHealthInsurance() {};

  @Test public void testUpdateInformation() {};

  @Test public void testUpdateAttendingHours() {};

  @Test public void testGetUsedHealthInsurances() {};

  @Test public void testGetUsedSpecialties() {};
}
