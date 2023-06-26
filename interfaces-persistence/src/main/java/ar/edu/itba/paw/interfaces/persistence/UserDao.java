package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import java.util.Locale;
import java.util.Optional;

public interface UserDao {

  // =============== Updates ===============

  public User updateUserInfo(
      long userId, String email, String firstName, String lastName, Image image, Locale locale)
      throws UserNotFoundException, EmailAlreadyExistsException;

  public String updateUserPassword(long userId, String password) throws UserNotFoundException;

  // =============== Queries ===============

  public Optional<User> getUserById(long id);

  public Optional<User> getUserByEmail(String email);
}
