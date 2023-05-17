package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
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

  private static final long ID = 1;
  private static final String EMAIL = "email";
  private static final String FIRST_NAME = "firstname";
  private static final String LAST_NAME = "lastname";
  private static final long PFP_ID = 1;
  private static final String PASSWORD = "password";
  private static final String PASSWORD_ENCODED = "password_encoded";

  @Mock private UserDao userDao;
  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserServiceImpl us;

  @Test
  public void testCreateUser() throws EmailAlreadyExistsException {
    // 1. Precondiciones
    // UserDao mock = Mockito.mock(UserDao.class);
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(
            userDao.createUser(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
        .thenReturn(new User(ID, EMAIL, PASSWORD_ENCODED, FIRST_NAME, LAST_NAME, PFP_ID));

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

  @Test(expected = RuntimeException.class)
  public void testCreateUserAlreadyExists() throws EmailAlreadyExistsException {
    // 1. Precondiciones
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(
            userDao.createUser(
                Mockito.eq(EMAIL),
                Mockito.eq(PASSWORD_ENCODED),
                Mockito.eq(FIRST_NAME),
                Mockito.eq(LAST_NAME)))
        .thenThrow(RuntimeException.class);

    // 2. Ejercitar la class under test
    us.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
  }

  @Test
  public void testGetUserById() {
    // 1. Precondiciones
    // UserDao mock = Mockito.mock(UserDao.class);
    Mockito.when(userDao.getUserById(Mockito.eq(ID)))
        .thenReturn(Optional.of(new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, PFP_ID)));
    // .thenThrow(SQLException.class);

    // 2. Ejercitar la class under test
    Optional<User> newUser = us.getUserById(ID);

    // 3. Meaningful assertions
    Assert.assertTrue(newUser.isPresent());
    Assert.assertEquals(ID, newUser.get().getId());
  }

  @Test
  public void testGetUserByIdUserDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetUserByEmail() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testGetUserByEmailUserDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testUpdateUser() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testUpdateUserDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testUpdatePassword() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testUpdatePasswordOldPasswordDoesNotMatch() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }

  @Test
  public void testUpdatePasswordUserDoesNotExist() {
    // 1. Precondiciones
    // 2. Ejercitar la class under test
    // 3. Meaninful assertions
  }
}
