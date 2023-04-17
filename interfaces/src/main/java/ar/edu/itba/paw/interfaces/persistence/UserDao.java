package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {

  User createUser(
      String email, String password, String firstName, String lastName, boolean isDoctor);

  void addHealthInsuranceToUser(long userId, long healthInsuranceId);

  Optional<User> findById(long id);
}
