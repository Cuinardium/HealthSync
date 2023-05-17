package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserService {

  // =============== Inserts ===============

  User createUser(String email, String password, String firstName, String lastName)
      throws EmailInUseException;

  // =============== Updates ===============

  User updateUser(long userId, String email, String firstName, String lastName, Image image);

  boolean updatePassword(long userId, String oldPassword, String password)
      throws IllegalStateException;

  // =============== Queries ===============

  Optional<User> getUserById(long id);

  Optional<User> getUserByEmail(String email);
}
