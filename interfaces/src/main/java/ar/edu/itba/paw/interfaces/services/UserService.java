package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserService {

  User createUser(String email, String password, String firstName, String lastName);

  Optional<User> findById(long id);
}
