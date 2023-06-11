package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserDao userDao;
  private final ImageService imageService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(
      UserDao userDao, ImageService imageService, final PasswordEncoder passwordEncoder) {
    this.userDao = userDao;
    this.imageService = imageService;
    this.passwordEncoder = passwordEncoder;
  }

  // =============== Inserts ===============

  @Transactional
  @Override
  public User createUser(
      String email, String password, String firstName, String lastName, Locale locale)
      throws EmailInUseException {
    try {
      return userDao.createUser(
          email, passwordEncoder.encode(password), firstName, lastName, locale);
    } catch (EmailAlreadyExistsException e) {
      throw new EmailInUseException();
    }
  }

  // =============== Updates ===============

  @Transactional
  @Override
  public User updateUser(
      long userId, String email, String firstName, String lastName, Image image, Locale locale)
      throws UserNotFoundException, EmailInUseException {

    Image old_image =
        userDao.getUserById(userId).orElseThrow(UserNotFoundException::new).getImage();

    try {

      if (image != null) {
        // Si la pfp es null -> insertamos imagen
        // si la pfp no es null -> la actualizamos para pisar la vieja
        if (image.getImageId() == null) {
          image = imageService.uploadImage(image);
        } else {

          image.setImageId(old_image.getImageId());
          image = imageService.updateImage(image);
        }
      }

      return userDao.updateUserInfo(
          userId, email, firstName, lastName, image == null ? old_image : image);

    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException e) {
      throw new IllegalStateException("User could not be updated because it does not exist");

    } catch (EmailAlreadyExistsException e) {
      throw new EmailInUseException();
    }
  }

  @Transactional
  @Override
  public boolean updatePassword(long userId, String oldPassword, String password)
      throws UserNotFoundException {

    String userPassword =
        userDao.getUserById(userId).orElseThrow(UserNotFoundException::new).getPassword();

    if (!passwordEncoder.matches(oldPassword, userPassword)) {
      // En clase vimos que era mejor retornar true o false, para verificar si se actualizo la pass
      return false;
    }

    try {
      // TODO: return true <-> hay afected rows?
      userDao.updateUserPassword(userId, passwordEncoder.encode(password));
      return true;

    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException e) {
      throw new IllegalStateException("User could not be updated because it does not exist");
    }
  }

  // =============== Queries ===============

  @Transactional
  @Override
  public Optional<User> getUserById(long id) {
    return userDao.getUserById(id);
  }

  @Transactional
  @Override
  public Optional<User> getUserByEmail(String email) {
    return userDao.getUserByEmail(email);
  }
}
