package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserDao userDao;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserDao userDao, final PasswordEncoder passwordEncoder) {
    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  @Override
  public User createUser(String email, String password, String firstName, String lastName) {
    return userDao.createUser(email, passwordEncoder.encode(password), firstName, lastName);
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
