package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.exceptions.EmailAlreadyExistsException;
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
public class UserDaoImplTest {
  private static final long ID = 1;
  private static final String EMAIL = "email";
  private static final String PASSWORD = "password";
  private static final String FIRST_NAME = "first_name";
  private static final String LAST_NAME = "last_name";
  private static final Long PFP_ID = null;

  private static final long ID2 = 2;
  private static final String EMAIL2 = "email2";
  private static final String PASSWORD2 = "password2";
  private static final String FIRST_NAME2 = "first_name2";
  private static final String LAST_NAME2 = "last_name2";

  @Autowired private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  @Autowired private UserDaoImpl userDao;

  @Before
  public void setUp() {
    jdbcTemplate = new JdbcTemplate(ds);
  }

  @Test
  public void testFindById() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.findById(ID);

    // 3. Meaningful assertions
    Assert.assertEquals(ID, maybeUser.get().getId());
    Assert.assertEquals(EMAIL, maybeUser.get().getEmail());
    Assert.assertEquals(PASSWORD, maybeUser.get().getPassword());
    Assert.assertEquals(FIRST_NAME, maybeUser.get().getFirstName());
    Assert.assertEquals(LAST_NAME, maybeUser.get().getLastName());
    Assert.assertEquals(PFP_ID, maybeUser.get().getProfilePictureId());
  }

  @Test
  public void testFindByIdDoesNotExist() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.findById(ID2);

    // 3. Meaningful assertions
    Assert.assertFalse(maybeUser.isPresent());
  }

  @Test
  public void testFindByEmail() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.findByEmail(EMAIL);

    // 3. Meaningful assertions
    Assert.assertEquals(ID, maybeUser.get().getId());
    Assert.assertEquals(EMAIL, maybeUser.get().getEmail());
    Assert.assertEquals(PASSWORD, maybeUser.get().getPassword());
    Assert.assertEquals(FIRST_NAME, maybeUser.get().getFirstName());
    Assert.assertEquals(LAST_NAME, maybeUser.get().getLastName());
    Assert.assertEquals(PFP_ID, maybeUser.get().getProfilePictureId());
  }

  @Test
  public void testFindByEmailDoesNotExist() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.findByEmail(EMAIL2);

    // 3. Meaningful assertions
    Assert.assertFalse(maybeUser.isPresent());
  }

  @Test
  public void testCreateUser() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    User user = userDao.createUser(EMAIL2, PASSWORD2, FIRST_NAME2, LAST_NAME2);

    // 3. Meaningful assertions
    Assert.assertNotNull(user);
    Assert.assertEquals(EMAIL2, user.getEmail());
    Assert.assertEquals(PASSWORD2, user.getPassword());
    Assert.assertEquals(FIRST_NAME2, user.getFirstName());
    Assert.assertEquals(LAST_NAME2, user.getLastName());

    Assert.assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
  }

  @Test
  public void testCreateUserAlreadyExists() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    assertThrows(
        EmailAlreadyExistsException.class,
        () -> userDao.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME));

    // 3. Meaningful assertions
  }

  @Test
  public void testEditUser() {
    // 1. Precondiciones (script testUsers.sql)
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testEditUserDoesNotExist() {
    // 1. Precondiciones (script testUsers.sql)
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testChangePassword() {
    // 1. Precondiciones (script testUsers.sql)
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }

  @Test
  public void testChangePasswordUserDoesNotExist() {
    // 1. Precondiciones (script testUsers.sql)
    // 2. Ejercitar la class under test
    // 3. Meaningful assertions
  }
}
