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
public class ImageDaoImplTest {

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private ImageDaoImpl imageDao;

  @Test public void testGetImage() {};

  @Test public void testUploadImage() {};

  @Test public void testUpdateImage() {};

}
