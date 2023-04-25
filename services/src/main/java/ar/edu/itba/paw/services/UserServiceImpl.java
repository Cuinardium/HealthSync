package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.HealthInsuranceService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserDao userDao;
  private final PasswordEncoder passwordEncoder;
  private final HealthInsuranceService healthInsuranceService;

  @Autowired
  public UserServiceImpl(UserDao userDao,final PasswordEncoder passwordEncoder, HealthInsuranceService healthInsuranceService) {
    this.userDao = userDao;
    this.passwordEncoder = passwordEncoder;
    this.healthInsuranceService = healthInsuranceService;
  }

  @Override
  public User createUser(String email, String password, String firstName, String lastName, String healthInsurance) {

    User user = userDao.createUser(email, passwordEncoder.encode(password), firstName, lastName, false);

    HealthInsurance insurance = healthInsuranceService.createHealthInsurance(healthInsurance);

    userDao.addHealthInsuranceToUser(user.getId(), insurance.getId());

    return user;
  }

  //TODO sacarlo
  @Override
  public User createUser(String email, String firstName, String lastName, String healthInsurance) {
    String password = UUID.randomUUID().toString().replace("-", "");
    return this.createUser(email, password, firstName, lastName, healthInsurance);
  }

  @Override
  public Optional<User> findById(long id) {
    return userDao.findById(id);
  }

  @Override
  public Optional<User> findByEmail(String email){
    return userDao.findByEmail(email);

  }
}
