package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.HealthInsuranceService;
import ar.edu.itba.paw.models.HealthInsurance;
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
  private static final String HEALTH_INSURANCE = "healthInsurance";
  private static final long PFP_ID = 1;
  private static final Boolean IS_DOCTOR = false;
  private static final String PASSWORD = "password";
  private static final String PASSWORD_ENCODED = "password_encoded";

  @Mock private UserDao userDao;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private HealthInsuranceService healthInsuranceService;

  @InjectMocks private UserServiceImpl us;

  @Test
  public void testCreateUser() {
    // 1. Precondiciones
    // UserDao mock = Mockito.mock(UserDao.class);
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(
            userDao.createUser(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyBoolean()))
        .thenReturn(
            new User(ID, EMAIL, PASSWORD_ENCODED, FIRST_NAME, LAST_NAME, IS_DOCTOR, PFP_ID));
    Mockito.when(healthInsuranceService.createHealthInsurance(Mockito.anyString()))
        .thenReturn(new HealthInsurance(1, HEALTH_INSURANCE));

    // TODO: mock this?
    // userDao.addHealthInsuranceToUser(user.getId(), insurance.getId());

    // 2. Ejercitar la class under test
    User newUser = us.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE);

    // 3. Meaningful assertions
    Assert.assertNotNull(newUser);
    Assert.assertEquals(EMAIL, newUser.getEmail());
    Assert.assertEquals(PASSWORD_ENCODED, newUser.getPassword());
    Assert.assertEquals(FIRST_NAME, newUser.getFirstName());
    Assert.assertEquals(LAST_NAME, newUser.getLastName());
    Assert.assertEquals(IS_DOCTOR, newUser.isDoctor());
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAlreadyExists() {
    // 1. Precondiciones
    Mockito.when(passwordEncoder.encode(Mockito.eq(PASSWORD))).thenReturn(PASSWORD_ENCODED);
    Mockito.when(
            userDao.createUser(
                Mockito.eq(EMAIL),
                Mockito.eq(PASSWORD_ENCODED),
                Mockito.eq(FIRST_NAME),
                Mockito.eq(LAST_NAME),
                Mockito.eq(IS_DOCTOR)))
        .thenThrow(RuntimeException.class);

    // 2. Ejercitar la class under test
    us.createUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, HEALTH_INSURANCE);
  }

  @Test
  public void testFindById() {
    // 1. Precondiciones
    // UserDao mock = Mockito.mock(UserDao.class);
    Mockito.when(userDao.findById(Mockito.eq(ID)))
        .thenReturn(
            Optional.of(new User(ID, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, IS_DOCTOR, PFP_ID)));
    // .thenThrow(SQLException.class);

    // 2. Ejercitar la class under test
    Optional<User> newUser = us.findById(ID);

    // 3. Meaningful assertions
    Assert.assertTrue(newUser.isPresent());
    Assert.assertEquals(ID, newUser.get().getId());
  }
}
