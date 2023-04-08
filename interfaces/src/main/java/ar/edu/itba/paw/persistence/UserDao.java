package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {
  User create(String email, String password);

  Optional<User> findById(long id);

  String getEmail(int id);
}
