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

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private UserDaoImpl userDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
    JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
  }

  @Test
  public void testFindById() throws SQLException {
    // 1. Precondiciones
    jdbcTemplate.execute(
        "INSERT INTO users (userid, email, password) VALUES ("
            + ID
            + ", '"
            + EMAIL
            + "', '"
            + PASSWORD
            + "');");

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.findById(ID);

    // 3. Meaningful assertions
    Assert.assertEquals(ID, maybeUser.get().getId());
    Assert.assertEquals(EMAIL, maybeUser.get().getEmail());
    Assert.assertEquals(PASSWORD, maybeUser.get().getPassword());
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
  public void testCreate() {
    // 1. Precondiciones

    // 2. Ejercitar la class under test
    User user = userDao.create(EMAIL, PASSWORD);

    // 3. Meaningful assertions
    Assert.assertNotNull(user);
    Assert.assertEquals(EMAIL, user.getEmail());
    Assert.assertEquals(PASSWORD, user.getPassword());

    Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
  }
}
