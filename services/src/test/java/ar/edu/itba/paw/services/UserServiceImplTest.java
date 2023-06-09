package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  private static final Long ID = 1L;
  private static final String EMAIL = "email";
  private static final String FIRST_NAME = "firstname";
  private static final String LAST_NAME = "lastname";
  private static final Image IMAGE = new Image(1L, new byte[0]);
  private static final String PASSWORD = "password";
  private static final String PASSWORD_ENCODED = "password_encoded";
  private static final String NOT_PASSWORD = "not_password";
  private static final String PASSWORD_NEW = "password_new";
  private static final String PASSWORD_NEW_ENCODED = "password_new_encoded";

  private static final String EMAIL_NEW = "email_new";
  private static final String FIRST_NAME_NEW = "firstname_new";
  private static final String LAST_NAME_NEW = "lastname_new";
  private static final Image IMAGE_NEW = new Image(1L, new byte[] {1, 4});

  @Mock private UserDao userDao;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private ImageService imageService; // imageservice.cualquerCosa() lo necesita

  @InjectMocks private UserServiceImpl us;

  // ====================== Create user ======================

  @Test
  public void testCreateUser() throws EmailAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    // UserDao mock = Mockito.mock(UserDao.class);
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(
            userDao.createUser(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(new User(ID, EMAIL, PASSWORD_ENCODED, FIRST_NAME, LAST_NAME, IMAGE));

    // TODO: mock this?
    // userDao.addHealthInsuranceToUser(user.getId(), insurance.getId());

    // 2. Ejercitar la class under test
    User newUser = us.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);

    // 3. Meaningful assertions
    Assert.assertNotNull(newUser);
    Assert.assertEquals(EMAIL, newUser.getEmail());
    Assert.assertEquals(PASSWORD_ENCODED, newUser.getPassword());
    Assert.assertEquals(FIRST_NAME, newUser.getFirstName());
    Assert.assertEquals(LAST_NAME, newUser.getLastName());
  }

  @Test(expected = EmailInUseException.class)
  public void testCreateUserAlreadyExists()
      throws EmailAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(
            userDao.createUser(
                Mockito.eq(EMAIL),
                Mockito.eq(PASSWORD_ENCODED),
                Mockito.eq(FIRST_NAME),
                Mockito.eq(LAST_NAME)))
        .thenThrow(EmailAlreadyExistsException.class);

    // 2. Ejercitar la class under test
    us.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
  }

  // ============================== Get user by id ==============================

  @Test
  public void testGetUserById() {
    // 1. Precondiciones
    // UserDao mock = Mockito.mock(UserDao.class);
    Mockito.when(userDao.getUserById(Mockito.eq(ID)))
        .thenReturn(Optional.of(new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, IMAGE)));
    // .thenThrow(SQLException.class);

    // 2. Ejercitar la class under test
    Optional<User> user = us.getUserById(ID);

    // 3. Meaningful assertions
    Assert.assertTrue(user.isPresent());
    Assert.assertEquals(ID, user.get().getId());
  }

  @Test
  public void testGetUserByIdUserDoesNotExist() {
    // 1. Precondiciones
    Mockito.when(userDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.empty());
    // 2. Ejercitar la class under test
    Optional<User> noUser = us.getUserById(ID);
    // 3. Meaningful assertions
    Assert.assertFalse(noUser.isPresent());
  }

  // ============================== Get user by email ==============================

  @Test
  public void testGetUserByEmail() {
    // 1. Precondiciones
    User expectedUser = new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, IMAGE);
    Mockito.when(userDao.getUserByEmail(Mockito.eq(EMAIL))).thenReturn(Optional.of(expectedUser));
    // 2. Ejercitar la class under test
    Optional<User> user = us.getUserByEmail(EMAIL);
    // 3. Meaningful assertions
    Assert.assertTrue(user.isPresent());
    Assert.assertEquals(expectedUser, user.get());
  }

  @Test
  public void testGetUserByEmailUserDoesNotExist() {
    // 1. Precondiciones
    Mockito.when(userDao.getUserByEmail(Mockito.eq(EMAIL))).thenReturn(Optional.empty());
    // 2. Ejercitar la class under test
    Optional<User> noUser = us.getUserByEmail(EMAIL);
    // 3. Meaningful assertions
    Assert.assertFalse(noUser.isPresent());
  }

  // ============================== Update user ==============================

  @Test
  public void testUpdateUser()
      throws UserNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException,
          EmailAlreadyExistsException, EmailInUseException {
    // 1. Precondiciones
    User USER = new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, IMAGE);
    User USER_UPDATED =
        new User(ID, EMAIL_NEW, PASSWORD_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE_NEW);
    Mockito.when(userDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(USER));
    Mockito.when(imageService.updateImage(IMAGE_NEW)).thenReturn(IMAGE_NEW);
    Mockito.when(
            userDao.updateUserInfo(
                Mockito.eq(ID),
                Mockito.eq(EMAIL_NEW),
                Mockito.eq(FIRST_NAME_NEW),
                Mockito.eq(LAST_NAME_NEW),
                Mockito.eq(IMAGE_NEW)))
        .thenReturn(USER_UPDATED);
    // 2. Ejercitar la class under test
    User user = us.updateUser(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE_NEW);
    // 3. Meaningful assertions
    Assert.assertEquals(USER_UPDATED, user);
  }

  @Test
  public void testUpdateUserWithoutUpdatingEmail() throws ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException, EmailInUseException, UserNotFoundException, EmailAlreadyExistsException {
    // 1. Precondiciones
    User USER = new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, IMAGE);
    User USER_UPDATED =
        new User(ID, EMAIL, PASSWORD_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE_NEW);

    Mockito.when(imageService.updateImage(IMAGE_NEW)).thenReturn(IMAGE_NEW);

    Mockito.when(userDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(USER));
    Mockito.when(
            userDao.updateUserInfo(
                Mockito.eq(ID),
                Mockito.eq(EMAIL),
                Mockito.eq(FIRST_NAME_NEW),
                Mockito.eq(LAST_NAME_NEW),
                Mockito.eq(IMAGE_NEW)))
        .thenReturn(USER_UPDATED);

    // 2. Ejercitar la class under test
    User user = us.updateUser(ID, EMAIL, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE_NEW);

    // 3. Meaningful assertions
    Assert.assertEquals(USER_UPDATED, user);
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException.class)
  public void testUpdateUserDoesNotExist()
      throws ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException,
          EmailInUseException {

    // 1. Precondiciones
    Mockito.when(userDao.getUserById(ID)).thenReturn(Optional.empty());

    // 2. Ejercitar la class under test
    us.updateUser(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE_NEW);
  }

  @Test(expected = EmailInUseException.class)
  public void testUpdateUserEmailAlreadyExists()
      throws EmailAlreadyExistsException, EmailInUseException, UserNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException {

    // 1. Precondiciones
    User USER = new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, IMAGE);
    Mockito.when(userDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(USER));
    Mockito.when(
            userDao.updateUserInfo(
                Mockito.eq(ID),
                Mockito.eq(EMAIL_NEW),
                Mockito.eq(FIRST_NAME_NEW),
                Mockito.eq(LAST_NAME_NEW),
                Mockito.eq(IMAGE_NEW)))
        .thenThrow(EmailAlreadyExistsException.class);

    // 2. Ejercitar la class under test
    us.updateUser(ID, EMAIL_NEW, FIRST_NAME_NEW, LAST_NAME_NEW, IMAGE_NEW);
  }

  // ============================== Update password ==============================

  @Test
  public void testUpdatePassword()
      throws UserNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException {

    // 1. Precondiciones
    User USER = new User(ID, EMAIL, PASSWORD_ENCODED, FIRST_NAME, LAST_NAME, IMAGE);
    User USER_UPDATED = new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, IMAGE_NEW);

    Mockito.when(passwordEncoder.matches(Mockito.eq(PASSWORD), Mockito.eq(PASSWORD_ENCODED)))
        .thenReturn(true);
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD_NEW))).thenReturn(PASSWORD_NEW_ENCODED);

    Mockito.when(userDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(USER));
    Mockito.when(userDao.updateUserPassword(Mockito.eq(ID), Mockito.eq(PASSWORD_NEW_ENCODED)))
        .thenReturn(USER_UPDATED.getPassword());

    // 2. Ejercitar la class under test
    boolean passwordUpdated = us.updatePassword(ID, PASSWORD, PASSWORD_NEW);

    // 3. Meaningful assertions
    Assert.assertTrue(passwordUpdated);
  }

  @Test
  public void testUpdatePasswordOldPasswordDoesNotMatch()
      throws ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException {

    // 1. Precondiciones
    User USER = new User(ID, EMAIL, PASSWORD_ENCODED, FIRST_NAME, LAST_NAME, IMAGE);

    Mockito.when(passwordEncoder.matches(Mockito.eq(NOT_PASSWORD), Mockito.eq(PASSWORD_ENCODED)))
        .thenReturn(false);

    Mockito.when(userDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(USER));

    // 2. Ejercitar la class under test
    boolean passwordUpdated = us.updatePassword(ID, NOT_PASSWORD, PASSWORD_NEW);

    // 3. Meaningful assertions
    Assert.assertFalse(passwordUpdated);
  }

  @Test(expected = ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException.class)
  public void testUpdatePasswordUserDoesNotExist()
      throws UserNotFoundException,
          ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException {

    // 1. Precondiciones
    Mockito.when(userDao.getUserById(Mockito.eq(ID)))
        .thenReturn(Optional.empty());

    // 2. Ejercitar la class under test
    us.updatePassword(ID, PASSWORD, PASSWORD_NEW);
  }
}
