package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {

  // =============== Inserts ===============

  User createUser(String email, String password, String firstName, String lastName);

  // =============== Updates ===============

  User updateUserInfo(long userId, String email, String firstName, String lastName, Long pfpId);

  String updateUserPassword(long userId, String password);

  // =============== Queries ===============
  
  Optional<User> getUserById(long id);

  Optional<User> getUserByEmail(String email);
}
