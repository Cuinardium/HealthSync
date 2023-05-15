package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {

  User createUser(String email, String password, String firstName, String lastName);

  void editUser(long userId, String email, String firstName, String lastName);

  void changePassword(long userId, String password);

  Optional<User> findById(long id);

  Optional<User> findByEmail(String email);
}
