package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.HealthInsuranceService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserDao userDao;
  private final HealthInsuranceService healthInsuranceService;

  @Autowired
  public UserServiceImpl(UserDao userDao, HealthInsuranceService healthInsuranceService) {
    this.userDao = userDao;
    this.healthInsuranceService = healthInsuranceService;
  }

  @Override
  public User createUser(String email, String password, String firstName, String lastName, String healthInsurance) {

    User user = userDao.createUser(email, password, firstName, lastName, false);

    HealthInsurance insurance = healthInsuranceService.createHealthInsurance(healthInsurance);

    userDao.addHealthInsuranceToUser(user.getId(), insurance.getId());

    return user;
  }

  @Override
  public User createUser(String email, String firstName, String lastName, String healthInsurance) {
    String password = UUID.randomUUID().toString().replace("-", "");
    return this.createUser(email, password, firstName, lastName, healthInsurance);
  }

  @Override
  public Optional<User> findById(long id) {
    return userDao.findById(id);
  }
}
