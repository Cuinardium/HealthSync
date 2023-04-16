package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoImplTest {
  private static final long ID = 1;
  private static final String EMAIL = "email";
  private static final String PASSWORD = "password";
  private static final String FIRST_NAME = "firstname";
  private static final String LAST_NAME = "lastname";
  private static final long PFP_ID = 1;
  private static final boolean IS_DOCTOR = false;

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private UserDaoImpl userDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
    JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    JdbcTestUtils.deleteFromTables(jdbcTemplate, "profile_picture");

     jdbcTemplate.execute(
        "INSERT INTO profile_picture (profile_picture_id, profile_picture) VALUES ("
            + PFP_ID
            + ", "
            + "CAST('30' AS BINARY)"
            + ");");
  }

  @Test
  public void testFindById() throws SQLException {
    // 1. Precondiciones
   

    jdbcTemplate.execute(
        "INSERT INTO users (user_id, email, password, first_name, last_name, profile_picture_id,"
            + " is_doctor) VALUES ("
            + ID
            + ", '"
            + EMAIL
            + "', '"
            + PASSWORD
            + "', '"
            + FIRST_NAME
            + "', '"
            + LAST_NAME
            + "', '"
            + PFP_ID
            + "', '"
            + IS_DOCTOR
            + "');");

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.findById(ID);

    // 3. Meaningful assertions
    Assert.assertEquals(ID, maybeUser.get().getId());
    Assert.assertEquals(EMAIL, maybeUser.get().getEmail());
    Assert.assertEquals(PASSWORD, maybeUser.get().getPassword());
    Assert.assertEquals(FIRST_NAME, maybeUser.get().getFirstName());
    Assert.assertEquals(LAST_NAME, maybeUser.get().getLastName());
    Assert.assertEquals(PFP_ID, maybeUser.get().getProfilePictureId());
    Assert.assertEquals(IS_DOCTOR, maybeUser.get().isDoctor());
  }

  @Test
  public void testFindByIdDoesNotExist() throws SQLException {
    // 1. Precondiciones

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.findById(ID);

    // 3. Meaningful assertions
    Assert.assertFalse(maybeUser.isPresent());
  }

  @Test
  public void testCreateClient() {
    // 1. Precondiciones

    // 2. Ejercitar la class under test
    User user = userDao.createClient(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);

    // 3. Meaningful assertions
    Assert.assertNotNull(user);
    Assert.assertEquals(EMAIL, user.getEmail());
    Assert.assertEquals(PASSWORD, user.getPassword());
    Assert.assertEquals(FIRST_NAME, user.getFirstName());
    Assert.assertEquals(LAST_NAME, user.getLastName());

    Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
  }

  @Test
  public void testCreateDoctor() {
    // 1. Precondiciones

    // 2. Ejercitar la class under test
    User user = userDao.createDoctor(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);

    // 3. Meaningful assertions
    Assert.assertNotNull(user);
    Assert.assertEquals(EMAIL, user.getEmail());
    Assert.assertEquals(PASSWORD, user.getPassword());
    Assert.assertEquals(FIRST_NAME, user.getFirstName());
    Assert.assertEquals(LAST_NAME, user.getLastName());

    Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
  }
}
