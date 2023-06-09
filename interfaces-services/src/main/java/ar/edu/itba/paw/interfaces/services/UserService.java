package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserService {

  // =============== Inserts ===============

  public User createUser(String email, String password, String firstName, String lastName)
      throws EmailInUseException;

  // =============== Updates ===============

  public User updateUser(long userId, String email, String firstName, String lastName, Image image)
      throws UserNotFoundException, EmailInUseException;

  public boolean updatePassword(long userId, String oldPassword, String password)
      throws UserNotFoundException;

  // =============== Queries ===============

  public Optional<User> getUserById(long id);

  public Optional<User> getUserByEmail(String email);
}
