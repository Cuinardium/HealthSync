package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {

  // =============== Inserts ===============

  User createUser(String email, String password, String firstName, String lastName)
      throws EmailAlreadyExistsException;

  // =============== Updates ===============

  User updateUserInfo(long userId, String email, String firstName, String lastName, Long pfpId)
      throws UserNotFoundException;

  String updateUserPassword(long userId, String password) throws UserNotFoundException;

  // =============== Queries ===============

  Optional<User> getUserById(long id);

  Optional<User> getUserByEmail(String email);
}
