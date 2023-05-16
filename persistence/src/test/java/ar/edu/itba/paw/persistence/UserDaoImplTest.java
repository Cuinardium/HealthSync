package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.persistence.exceptions.UserNotFoundException;
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
  private static final long INSERTED_USER_ID = 1;
  private static final String INSERTED_USER_EMAIL = "patient@email.com";
  private static final String INSERTED_USER_PASSWORD = "patient_password";
  private static final String INSERTED_USER_FIRST_NAME = "patient_first_name";
  private static final String INSERTED_USER_LAST_NAME = "patient_last_name";
  private static final Long INSERTED_USER_PFP_ID = null;

  private static final long AUX_ID = 3;
  private static final String AUX_EMAIL = "notuser@email.com";
  private static final String AUX_PASSWORD = "notuser_password";
  private static final String AUX_FIRST_NAME = "notuser_first_name";
  private static final String AUX_LAST_NAME = "notuser_last_name";
  private static final Long AUX_PFP_ID = 1L;

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
    Optional<User> maybeUser = userDao.getUserById(INSERTED_USER_ID);

    // 3. Meaningful assertions
    Assert.assertEquals(INSERTED_USER_ID, maybeUser.get().getId());
    Assert.assertEquals(INSERTED_USER_EMAIL, maybeUser.get().getEmail());
    Assert.assertEquals(INSERTED_USER_PASSWORD, maybeUser.get().getPassword());
    Assert.assertEquals(INSERTED_USER_FIRST_NAME, maybeUser.get().getFirstName());
    Assert.assertEquals(INSERTED_USER_LAST_NAME, maybeUser.get().getLastName());
    Assert.assertEquals(INSERTED_USER_PFP_ID, maybeUser.get().getProfilePictureId());
  }

  @Test
  public void testFindByIdDoesNotExist() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.getUserById(AUX_ID);

    // 3. Meaningful assertions
    Assert.assertFalse(maybeUser.isPresent());
  }

  @Test
  public void testFindByEmail() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.getUserByEmail(INSERTED_USER_EMAIL);

    // 3. Meaningful assertions
    Assert.assertEquals(INSERTED_USER_ID, maybeUser.get().getId());
    Assert.assertEquals(INSERTED_USER_EMAIL, maybeUser.get().getEmail());
    Assert.assertEquals(INSERTED_USER_PASSWORD, maybeUser.get().getPassword());
    Assert.assertEquals(INSERTED_USER_FIRST_NAME, maybeUser.get().getFirstName());
    Assert.assertEquals(INSERTED_USER_LAST_NAME, maybeUser.get().getLastName());
    Assert.assertEquals(INSERTED_USER_PFP_ID, maybeUser.get().getProfilePictureId());
  }

  @Test
  public void testFindByEmailDoesNotExist() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.getUserByEmail(AUX_EMAIL);

    // 3. Meaningful assertions
    Assert.assertFalse(maybeUser.isPresent());
  }

  @Test
  public void testCreateUser() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    User user = userDao.createUser(AUX_EMAIL, AUX_PASSWORD, AUX_FIRST_NAME, AUX_LAST_NAME);

    // 3. Meaningful assertions
    Assert.assertNotNull(user);
    Assert.assertEquals(AUX_EMAIL, user.getEmail());
    Assert.assertEquals(AUX_PASSWORD, user.getPassword());
    Assert.assertEquals(AUX_FIRST_NAME, user.getFirstName());
    Assert.assertEquals(AUX_LAST_NAME, user.getLastName());
    Assert.assertEquals(INSERTED_USER_PFP_ID, user.getProfilePictureId());

    Assert.assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
  }

  @Test
  public void testCreateUserAlreadyExists() {
    // 1. Precondiciones (script testUsers.sql)

    // 2. Ejercitar la class under test
    assertThrows(
        EmailAlreadyExistsException.class,
        () ->
            userDao.createUser(
                INSERTED_USER_EMAIL,
                INSERTED_USER_PASSWORD,
                INSERTED_USER_FIRST_NAME,
                INSERTED_USER_LAST_NAME));

    // 3. Meaningful assertions
  }

  @Test
  public void testUpdateUserInfo() {
    // 1. Precondiciones (script testUsers.sql y testImages.sql)
    // 2. Ejercitar la class under test
    User user =
        userDao.updateUserInfo(
            INSERTED_USER_ID, AUX_EMAIL, AUX_FIRST_NAME, AUX_LAST_NAME, AUX_PFP_ID);

    // 3. Meaningful assertions
    Assert.assertNotNull(user);
    Assert.assertEquals(INSERTED_USER_ID, user.getId());
    Assert.assertEquals(AUX_EMAIL, user.getEmail());
    Assert.assertEquals(INSERTED_USER_PASSWORD, user.getPassword());
    Assert.assertEquals(AUX_FIRST_NAME, user.getFirstName());
    Assert.assertEquals(AUX_LAST_NAME, user.getLastName());
    Assert.assertEquals(AUX_PFP_ID, user.getProfilePictureId());
  }

  @Test
  public void testEditUserDoesNotExist() {
    // 1. Precondiciones (script testUsers.sql)
    // 2. Ejercitar la class under test
    assertThrows(
        UserNotFoundException.class,
        () -> userDao.updateUserInfo(AUX_ID, AUX_EMAIL, AUX_FIRST_NAME, AUX_LAST_NAME, AUX_PFP_ID));
    // 3. Meaningful assertions
  }

  @Test
  public void testUpdatePassword() {
    // 1. Precondiciones (script testUsers.sql)
    // 2. Ejercitar la class under test
    String password = userDao.updateUserPassword(INSERTED_USER_ID, AUX_PASSWORD);
    // 3. Meaningful assertions
    Assert.assertEquals(AUX_PASSWORD, password);
  }

  @Test
  public void testChangePasswordUserDoesNotExist() {
    // 1. Precondiciones (script testUsers.sql)
    // 2. Ejercitar la class under test
    assertThrows(
        UserNotFoundException.class, () -> userDao.updateUserPassword(AUX_ID, AUX_PASSWORD));
    // 3. Meaningful assertions
  }
}
