package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {

  User createUser(String email, String password, String firstName, String lastName);

  Optional<User> findById(long id);
}
