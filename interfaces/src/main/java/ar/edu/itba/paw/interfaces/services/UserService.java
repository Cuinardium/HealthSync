package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserService {

  User createUser(String email, String password, String firstName, String lastName);

  void editUser(long userId, String email, String firstName, String lastName);

  void changePassword(long userId, String oldPassword, String password);

  Optional<User> findById(long id);

  Optional<User> findByEmail(String email);
}
