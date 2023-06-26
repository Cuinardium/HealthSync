package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import java.util.Locale;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoJpa implements UserDao {

  @PersistenceContext private EntityManager em;

  @Override
  public User updateUserInfo(
      long userId, String email, String firstName, String lastName, Image image, Locale locale)
      throws UserNotFoundException, EmailAlreadyExistsException {
    User user = getUserById(userId).orElseThrow(UserNotFoundException::new);

    if (getUserByEmail(email).isPresent() && !user.getEmail().equals(email)) {
      throw new EmailAlreadyExistsException();
    }

    user.setEmail(email);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setImage(image);
    user.setLocale(locale);
    em.persist(user);
    return user;
  }

  @Override
  public String updateUserPassword(long userId, String password) throws UserNotFoundException {
    User user = getUserById(userId).orElseThrow(UserNotFoundException::new);
    user.setPassword(password);
    em.persist(user);
    return user.getPassword();
  }

  @Override
  public Optional<User> getUserById(long id) {
    return Optional.ofNullable(em.find(User.class, id));
  }

  @Override
  public Optional<User> getUserByEmail(String email) {
    // JPA Query Language (JQL) / Hibernate Query Language (HQL)
    final TypedQuery<User> query =
        em.createQuery("from User as u where u.email = :email", User.class);
    query.setParameter("email", email);

    return query.getResultList().stream().findFirst();
  }
}
