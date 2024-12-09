package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.TokenService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailInUseException;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenInvalidException;
import ar.edu.itba.paw.interfaces.services.exceptions.TokenNotFoundException;
import ar.edu.itba.paw.interfaces.services.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
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
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(
      final UserDao userDao,
      final ImageService imageService,
      final TokenService tokenService,
      final PasswordEncoder passwordEncoder) {
    this.userDao = userDao;
    this.imageService = imageService;
    this.tokenService = tokenService;
    this.passwordEncoder = passwordEncoder;
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
        image = imageService.uploadImage(image);
      }

      return userDao.updateUserInfo(
          userId, email, firstName, lastName, image == null ? old_image : image, locale);

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
      userDao.updateUserPassword(userId, passwordEncoder.encode(password));
      return true;

    } catch (ar.edu.itba.paw.interfaces.persistence.exceptions.UserNotFoundException e) {
      throw new IllegalStateException("User could not be updated because it does not exist");
    }
  }

  @Transactional
  @Override
  public void confirmUser(long userId, String token)
      throws UserNotFoundException, TokenNotFoundException, TokenInvalidException {
    final User user = userDao.getUserById(userId).orElseThrow(UserNotFoundException::new);

    final VerificationToken verificationToken =
        tokenService
            .getUserToken(user)
            .filter(t -> t.getToken().equals(token))
            .orElseThrow(TokenNotFoundException::new);

    if (verificationToken.isExpired()) {
      throw new TokenInvalidException();
    }

    user.setIsVerified(true);
    tokenService.deleteUserToken(user);
  }

  // =============== Queries ===============

  @Transactional(readOnly = true)
  @Override
  public Optional<User> getUserById(long id) {
    return userDao.getUserById(id);
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<User> getUserByEmail(String email) {
    return userDao.getUserByEmail(email);
  }
}
