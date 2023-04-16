package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;
import java.util.Optional;

public interface UserDao {

  User createDoctor(String email, String password, String firstName, String lastName);

  User createClient(String email, String password, String firstName, String lastName);

  Optional<User> findById(long id);
}
