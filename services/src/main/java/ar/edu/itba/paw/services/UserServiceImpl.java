package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserDao userDao;

  @Autowired
  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public User createUser(String email, String password) {
    return userDao.create(email, password);
  }

  @Override
  public String getEmail(int id) {
    return userDao.getEmail(id);
  }

  @Override
  public Optional<User> findById(long id) {
    return userDao.findById(id);
  }
}
