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

  private static final long DEFAULT_PFP_ID = 1;
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

  private static final String FIND_BY_ID =
      "SELECT * FROM users WHERE user_id = ?";

  private static final String FIND_BY_EMAIL =
          "SELECT * FROM users WHERE email = ?";


  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert userInsert;
  private final SimpleJdbcInsert userHealthInsuranceInsert;
  
  @Autowired
  public UserDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
    this.userInsert =
        new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("user_id");
    this.userHealthInsuranceInsert =
        new SimpleJdbcInsert(ds).withTableName("health_insurance_for_user");
  }

  @Override
  public User createUser(
      String email, String password, String firstName, String lastName, boolean isDoctor) {
    Map<String, Object> data = new HashMap<>();

    data.put("email", email);
    data.put("password", password);
    data.put("first_name", firstName);
    data.put("last_name", lastName);
    data.put("is_doctor", isDoctor);
    data.put("profile_picture_id", DEFAULT_PFP_ID);

    // Profile Picture is default for now

    final Number key = userInsert.executeAndReturnKey(data);
    return new User(
        key.longValue(), email, password, firstName, lastName, isDoctor, DEFAULT_PFP_ID);
  }


  @Override
  public void addHealthInsuranceToUser(long userId, long healthInsuranceId) {
    
    Map<String, Object> data = new HashMap<>();
    data.put("user_id", userId);
    data.put("health_insurance_id", healthInsuranceId);

    userHealthInsuranceInsert.execute(data);
  }

  // Optional garantiza que no va a ser null, pero no significa q se vaya a devolver un usuario
  @Override
  public Optional<User> findById(final long id) {
    // el ? "sanitiza" el parametro para evitar SQL Injection
    return jdbcTemplate.query(FIND_BY_ID, ROW_MAPPER, id).stream()
        .findFirst();
  }

  @Override
  public Optional<User> findByEmail(String email){
    return jdbcTemplate.query(FIND_BY_EMAIL, ROW_MAPPER, email).stream()
            .findFirst();
  }
}
