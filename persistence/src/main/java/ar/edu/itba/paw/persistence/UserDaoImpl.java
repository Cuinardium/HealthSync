package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.persistence.utils.QueryBuilder;
import ar.edu.itba.paw.persistence.utils.UpdateBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert userInsert;

  @Autowired
  public UserDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
    this.userInsert =
        new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("user_id");
  }

  @Override
  public User createUser(String email, String password, String firstName, String lastName)
      throws EmailAlreadyExistsException {
    Map<String, Object> data = new HashMap<>();

    data.put("email", email);
    data.put("password", password);
    data.put("first_name", firstName);
    data.put("last_name", lastName);

    try {
      final Number key = userInsert.executeAndReturnKey(data);
      return new User(key.longValue(), email, password, firstName, lastName, null);
    } catch (DuplicateKeyException e) {
      throw new EmailAlreadyExistsException();
    }
  }

  @Override
  public User updateUserInfo(
      long userId, String email, String firstName, String lastName, Long pfpId) {
    String update =
        new UpdateBuilder()
            .update("users")
            .set("email", "'" + email + "'")
            .set("first_name", "'" + firstName + "'")
            .set("last_name", "'" + lastName + "'")
            .set("profile_picture_id", pfpId.toString())
            .where("user_id = (" + userId + ")")
            .build();

    jdbcTemplate.update(update);
    return findById(userId).orElseThrow(IllegalStateException::new);
  }

  @Override
  public String updateUserPassword(long userId, String password) {
    String update =
        new UpdateBuilder()
            .update("users")
            .set("password", "'" + password + "'")
            .where("user_id = (" + userId + ")")
            .build();

    jdbcTemplate.update(update);
    return findById(userId).orElseThrow(IllegalStateException::new).getPassword();
  }

  // Optional garantiza que no va a ser null, pero no significa q se vaya a devolver un usuario
  @Override
  public Optional<User> findById(final long id) {

    String query = new QueryBuilder().select().from("users").where("user_id = " + id).build();

    return jdbcTemplate.query(query, RowMappers.USER_MAPPER).stream().findFirst();
  }

  @Override
  public Optional<User> findByEmail(String email) {
    String query =
        new QueryBuilder().select().from("users").where("email = '" + email + "'").build();
    return jdbcTemplate.query(query, RowMappers.USER_MAPPER).stream().findFirst();
  }
}
