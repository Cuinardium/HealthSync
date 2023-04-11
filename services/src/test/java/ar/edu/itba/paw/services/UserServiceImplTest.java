package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.UserDao;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

// Le permite a Mockito tomar control de JUnit y permite anotaciones que sino no estarian
// disponibles
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

  private static final int ID = 1;
  private static final String EMAIL = "email";
  private static final String PASSWORD = "password";
  // private final UserServiceImpl us = new UserServiceImpl(null);

  @Mock private UserDao userDao;

  @InjectMocks private UserServiceImpl us;

  @Test
  public void testCreate() {
    // 1. Precondiciones
    // UserDao mock = Mockito.mock(UserDao.class);
    Mockito.when(userDao.create(Mockito.anyString(), Mockito.anyString()))
        .thenReturn(new User(0, EMAIL, PASSWORD));

    // 2. Ejercitar la class under test
    User newUser = us.createUser(EMAIL, PASSWORD);

    // 3. Meaningful assertions
    Assert.assertNotNull(newUser);
    Assert.assertEquals(EMAIL, newUser.getEmail());
    Assert.assertEquals(PASSWORD, newUser.getPassword());
  }

  @Test(expected = RuntimeException.class)
  public void testCreateAlreadyExists() {
    // 1. Precondiciones
    Mockito.when(userDao.create(Mockito.eq(EMAIL), Mockito.eq(PASSWORD)))
        .thenThrow(RuntimeException.class);

    // 2. Ejercitar la class under test
    User newUser = us.createUser(EMAIL, PASSWORD);
  }

  @Test
  public void testFindById() {
    // 1. Precondiciones
    // UserDao mock = Mockito.mock(UserDao.class);
    Mockito.when(userDao.findById(Mockito.eq(ID)))
        .thenReturn(Optional.of(new User(ID, EMAIL, PASSWORD)));
    // .thenThrow(SQLException.class);

    // 2. Ejercitar la class under test
    Optional<User> newUser = us.findById(ID);

    // 3. Meaningful assertions
    Assert.assertTrue(newUser.isPresent());
    Assert.assertEquals(ID, newUser.get().getId());
  }
}
