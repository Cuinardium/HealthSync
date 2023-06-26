package ar.edu.itba.paw.persistence;

import static org.junit.Assert.assertThrows;

import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import java.util.Locale;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoImplTest {
  private static final Long INSERTED_USER_ID = 5L;
  private static final String INSERTED_USER_EMAIL = "patient@email.com";
  private static final String INSERTED_USER_PASSWORD = "patient_password";
  private static final String INSERTED_USER_FIRST_NAME = "patient_first_name";
  private static final String INSERTED_USER_LAST_NAME = "patient_last_name";
  private static final Image INSERTED_USER_IMAGE = null;

  private static final String ALREADY_INSERTED_MAIL = "doctor@email.com";

  private static final Long AUX_ID = 100L;
  private static final String AUX_EMAIL = "notuser@email.com";
  private static final String AUX_PASSWORD = "notuser_password";
  private static final String AUX_FIRST_NAME = "notuser_first_name";
  private static final String AUX_LAST_NAME = "notuser_last_name";
  private static final Image AUX_IMAGE = new Image(2L, null);
  private static final Locale AUX_LOCALE = new Locale("en");
  private static final Locale INSERTED_LOCALE = new Locale("en");

  @PersistenceContext private EntityManager em;

  @Autowired private UserDaoJpa userDao;

  @Test
  public void testGetUserById() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.getUserById(INSERTED_USER_ID);

    // 3. Meaningful assertions
    Assert.assertEquals(INSERTED_USER_ID, maybeUser.get().getId());
    Assert.assertEquals(INSERTED_USER_EMAIL, maybeUser.get().getEmail());
    Assert.assertEquals(INSERTED_USER_PASSWORD, maybeUser.get().getPassword());
    Assert.assertEquals(INSERTED_USER_FIRST_NAME, maybeUser.get().getFirstName());
    Assert.assertEquals(INSERTED_USER_LAST_NAME, maybeUser.get().getLastName());
    Assert.assertEquals(INSERTED_USER_IMAGE, maybeUser.get().getImage());
    Assert.assertEquals(INSERTED_LOCALE, maybeUser.get().getLocale());
  }

  @Test
  public void testGetUserByIdDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.getUserById(AUX_ID);

    // 3. Meaningful assertions
    Assert.assertFalse(maybeUser.isPresent());
  }

  @Test
  public void testGetUserByEmail() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.getUserByEmail(INSERTED_USER_EMAIL);

    // 3. Meaningful assertions
    Assert.assertEquals(INSERTED_USER_ID, maybeUser.get().getId());
    Assert.assertEquals(INSERTED_USER_EMAIL, maybeUser.get().getEmail());
    Assert.assertEquals(INSERTED_USER_PASSWORD, maybeUser.get().getPassword());
    Assert.assertEquals(INSERTED_USER_FIRST_NAME, maybeUser.get().getFirstName());
    Assert.assertEquals(INSERTED_USER_LAST_NAME, maybeUser.get().getLastName());
    Assert.assertEquals(INSERTED_USER_IMAGE, maybeUser.get().getImage());
    Assert.assertEquals(INSERTED_LOCALE, maybeUser.get().getLocale());
  }

  @Test
  public void testGetUserByEmailDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    Optional<User> maybeUser = userDao.getUserByEmail(AUX_EMAIL);

    // 3. Meaningful assertions
    Assert.assertFalse(maybeUser.isPresent());
  }

  @Test
  public void testUpdateUserInfo() throws UserNotFoundException, EmailAlreadyExistsException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    User user =
        userDao.updateUserInfo(
            INSERTED_USER_ID, AUX_EMAIL, AUX_FIRST_NAME, AUX_LAST_NAME, AUX_IMAGE, AUX_LOCALE);

    // 3. Meaningful assertions
    Assert.assertNotNull(user);
    Assert.assertEquals(INSERTED_USER_ID, user.getId());
    Assert.assertEquals(AUX_EMAIL, user.getEmail());
    Assert.assertEquals(INSERTED_USER_PASSWORD, user.getPassword());
    Assert.assertEquals(AUX_FIRST_NAME, user.getFirstName());
    Assert.assertEquals(AUX_LAST_NAME, user.getLastName());
    Assert.assertEquals(AUX_IMAGE, user.getImage());
    Assert.assertEquals(AUX_LOCALE, user.getLocale());
  }

  @Test
  public void testUpdateUserInfoUserDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        UserNotFoundException.class,
        () ->
            userDao.updateUserInfo(
                AUX_ID, AUX_EMAIL, AUX_FIRST_NAME, AUX_LAST_NAME, AUX_IMAGE, AUX_LOCALE));
    // 3. Meaningful assertions
  }

  @Test
  public void testUpdateUserInfoAlreadyExists() {

    // 1. Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        EmailAlreadyExistsException.class,
        () ->
            userDao.updateUserInfo(
                INSERTED_USER_ID,
                ALREADY_INSERTED_MAIL,
                INSERTED_USER_FIRST_NAME,
                INSERTED_USER_LAST_NAME,
                INSERTED_USER_IMAGE,
                INSERTED_LOCALE));
    // 3. Meaningful assertions
  }

  @Test
  public void testUpdatePassword() throws UserNotFoundException {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    String password = userDao.updateUserPassword(INSERTED_USER_ID, AUX_PASSWORD);
    // 3. Meaningful assertions
    Assert.assertEquals(AUX_PASSWORD, password);
  }

  @Test
  public void testUpdatePasswordUserDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    assertThrows(
        UserNotFoundException.class, () -> userDao.updateUserPassword(AUX_ID, AUX_PASSWORD));
    // 3. Meaningful assertions
  }
}
