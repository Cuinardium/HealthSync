package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import java.util.*;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert jdbcInsert;
  private static final RowMapper<User> ROW_MAPPER =
      (rs, rowNum) ->
          new User(
              rs.getLong("user_id"),
              rs.getString("email"),
              rs.getString("password"),
              rs.getString("first_name"),
              rs.getString("last_name"),
              rs.getBoolean("is_doctor"),
              rs.getLong("profile_picture_id"));

  @Autowired
  public UserDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
    this.jdbcInsert =
        new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("user_id");
  }

  private User createUser(
      String email, String password, String firstName, String lastName, Boolean isDoctor) {
    Map<String, Object> data = new HashMap<>();

    data.put("email", email);
    data.put("password", password);
    data.put("first_name", firstName);
    data.put("last_name", lastName);
    data.put("is_doctor", isDoctor);
    data.put("profile_picture_id", 1);

    // Profile Picture is default for now

    final Number key = jdbcInsert.executeAndReturnKey(data);
    return new User(key.longValue(), email, password, firstName, lastName, isDoctor, 1);
  }

  @Override
  public User createClient(String email, String password, String firstName, String lastName) {
    return createUser(email, password, firstName, lastName, false);
  }

  @Override
  public User createDoctor(String email, String password, String firstName, String lastName) {
    // TODO: agregar el resto de los campos de medico (lugar y eso)
    return createUser(email, password, firstName, lastName, true);
  }

  // Optional garantiza que no va a ser null, pero no significa q se vaya a devolver un usuario
  @Override
  public Optional<User> findById(final long id) {
    // el ? "sanitiza" el parametro para evitar SQL Injection
    return jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?", ROW_MAPPER, id).stream()
        .findFirst();
  }
}
