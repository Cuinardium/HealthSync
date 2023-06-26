package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenInvalidException;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import java.util.Locale;
import java.util.Optional;

public interface UserService {

  // =============== Updates ===============

  public User updateUser(
      long userId, String email, String firstName, String lastName, Image image, Locale locale)
      throws UserNotFoundException, EmailInUseException;

  public boolean updatePassword(long userId, String oldPassword, String password)
      throws UserNotFoundException;

  public void confirmUser(long userId, String token) throws UserNotFoundException, TokenNotFoundException, TokenInvalidException;

  // =============== Queries ===============

  public Optional<User> getUserById(long id);

  public Optional<User> getUserByEmail(String email);
}
