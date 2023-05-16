package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {

  User createUser(String email, String password, String firstName, String lastName);

  User updateUserInfo(long userId, String email, String firstName, String lastName, Long pfpId);

  String updateUserPassword(long userId, String password);

  Optional<User> findById(long id);

  Optional<User> findByEmail(String email);
}
