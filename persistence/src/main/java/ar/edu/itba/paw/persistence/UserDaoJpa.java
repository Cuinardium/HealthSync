package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.User;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoJpa implements UserDao {

  @PersistenceContext private EntityManager em;

  @Override
  public User createUser(String email, String password, String firstName, String lastName)
      throws EmailAlreadyExistsException {
    final User user = new User(null, email, password, firstName, lastName, null);
    em.persist(user);
    return user;
  }

  @Override
  public User updateUserInfo(
      long userId, String email, String firstName, String lastName, Long pfpId)
      throws UserNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateUserInfo'");
  }

  @Override
  public String updateUserPassword(long userId, String password) throws UserNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateUserPassword'");
  }

  @Override
  public Optional<User> getUserById(long id) {
    return Optional.ofNullable(em.find(User.class, id));
  }

  @Override
  public Optional<User> getUserByEmail(String email) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getUserByEmail'");
  }
}
