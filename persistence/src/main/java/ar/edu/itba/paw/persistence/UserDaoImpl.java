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
          new User(rs.getLong("user_id"), rs.getString("email"), rs.getString("password"));

  @Autowired
  public UserDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
    this.jdbcInsert =
        new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("userid");
  }

  @Override
  public User create(String email, String password) {
    Map<String, Object> data = new HashMap<>();
    data.put("email", email);
    data.put("password", password);
    final Number key = jdbcInsert.executeAndReturnKey(data);
    return new User(key.longValue(), email, password);
  }

  @Override
  public String getEmail(int id) {
    return "sballerini@itba.edu.ar";
  }
  // for testing purposes the static email will be sballerini@itba.edu.ar for the time being

  // Optional garantiza que no va a ser null, pero no significa q se vaya a devolver un usuario
  @Override
  public Optional<User> findById(final long id) {
    // el ? "sanitiza" el parametro para evitar SQL Injection
    return jdbcTemplate
        .query("SELECT * FROM users WHERE user_id = ?", ROW_MAPPER, id)
        .stream()
        .findFirst();
  }
}
