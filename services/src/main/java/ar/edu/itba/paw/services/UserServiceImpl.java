package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
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
  public User createUser(String email, String password, String firstName, String lastName)
      throws EmailInUseException {
    try {
      return userDao.createUser(email, passwordEncoder.encode(password), firstName, lastName);
    } catch (EmailAlreadyExistsException e) {
      throw new EmailInUseException();
    }
  }

  // =============== Updates ===============

  @Transactional
  @Override
  public User updateUser(long userId, String email, String firstName, String lastName, Image image)
      throws UserNotFoundException {

    try {
      Long pfpId =
          userDao.getUserById(userId).orElseThrow(IllegalStateException::new).getProfilePictureId();
      if (image != null) {
        // Si la pfp es null -> insertamos imagen
        // si la pfp no es null -> la actualizamos para pisar la vieja
        if (pfpId == null) {
          pfpId = imageService.uploadImage(image);
        } else {
          imageService.updateImage(pfpId, image);
        }
      }
      return userDao.updateUserInfo(userId, email, firstName, lastName, pfpId);
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException e) {
      throw new UserNotFoundException();
    }
  }

  @Transactional
  @Override
  public boolean updatePassword(long userId, String oldPassword, String password)
      throws IllegalStateException, UserNotFoundException {
    if (!passwordEncoder.matches(
        oldPassword,
        userDao.getUserById(userId).orElseThrow(IllegalStateException::new).getPassword())) {
      // En clase vimos que era mejor retornar true o false, para verificar si se actualizo la pass
      return false;
    }
    try {
      // TODO: return true <-> hay afected rows?
      userDao.updateUserPassword(userId, passwordEncoder.encode(password));
      return true;
    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException e) {
      throw new UserNotFoundException();
    }
  }

  // =============== Queries ===============

  @Override
  public Optional<User> getUserById(long id) {
    return userDao.getUserById(id);
  }

  @Override
  public Optional<User> getUserByEmail(String email) {
    return userDao.getUserByEmail(email);
  }
}
