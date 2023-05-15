package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
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

  @Transactional
  @Override
  public User createUser(String email, String password, String firstName, String lastName) {
    return userDao.createUser(email, passwordEncoder.encode(password), firstName, lastName);
  }

  @Transactional
  @Override
  public void editUser(long userId, String email, String firstName, String lastName, Image image) {
    Long pfpId =
        userDao.findById(userId).orElseThrow(IllegalStateException::new).getProfilePictureId();
    // Si la pfp es null -> insertamos imagen
    // si la pfp no es null -> la actualizamos para pisar la vieja
    if (pfpId == null) {
      pfpId = imageService.uploadImage(image);
    } else {
      imageService.updateImage(pfpId, image);
    }
    userDao.editUser(userId, email, firstName, lastName, pfpId);
  }

  @Override
  public boolean changePassword(long userId, String oldPassword, String password)
      throws IllegalStateException {
    if (!passwordEncoder.matches(
        oldPassword,
        userDao.findById(userId).orElseThrow(IllegalStateException::new).getPassword())) {
      // En clase vimos que era mejor retornar true o false, para verificar si se actualizo la pass
      return false;
    }
    // TODO: return true <-> hay afected rows?
    userDao.changePassword(userId, passwordEncoder.encode(password));
    return true;
  }

  @Override
  public Optional<User> findById(long id) {
    return userDao.findById(id);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userDao.findByEmail(email);
  }
}
