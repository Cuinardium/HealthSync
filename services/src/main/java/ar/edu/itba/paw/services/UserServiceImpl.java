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
  public User createClient(String email, String password, String firstName, String lastName) {
    return userDao.createClient(email, password, firstName, lastName);
  }

  @Override
  public User createDoctor(String email, String password, String firstName, String lastName) {
    return userDao.createDoctor(email, password, firstName, lastName);
  }

  @Override
  public Optional<User> findById(long id) {
    return userDao.findById(id);
  }
}
