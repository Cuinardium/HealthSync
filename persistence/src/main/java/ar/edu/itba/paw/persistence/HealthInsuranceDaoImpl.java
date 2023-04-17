package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.HealthInsuranceDao;
import ar.edu.itba.paw.models.HealthInsurance;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class HealthInsuranceDaoImpl implements HealthInsuranceDao {

  private static final String GET_HEALTH_INSURANCE_BY_ID = "SELECT * FROM health_insurance WHERE health_insurance_id = ?";
  private static final String GET_HEALTH_INSURANCE_BY_NAME = "SELECT * FROM health_insurance WHERE health_insurance_name = ?";

  private static final RowMapper<HealthInsurance> ROW_MAPPER = (rs, rowNum) -> new HealthInsurance(
      rs.getLong("health_insurance_id"),
      rs.getString("health_insurance_name")
  );

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert jdbcInsert;

  @Autowired
  public HealthInsuranceDaoImpl(DataSource ds) {
    jdbcTemplate = new JdbcTemplate(ds);
    jdbcInsert =
        new SimpleJdbcInsert(ds)
            .withTableName("health_insurance")
            .usingGeneratedKeyColumns("health_insurance_id");
  }

  @Override
  public HealthInsurance createHealthInsurance(String name) {

    Map<String, Object> args = new HashMap<>();

    args.put("health_insurance_name", name);

    Number id = jdbcInsert.executeAndReturnKey(args);

    return new HealthInsurance(id.longValue(), name);
  }

  @Override
  public Optional<HealthInsurance> getHealthInsuranceById(long id) {
    return jdbcTemplate.query(GET_HEALTH_INSURANCE_BY_ID, ROW_MAPPER, id).stream().findFirst();
  }

  @Override
  public Optional<HealthInsurance> getHealthInsuranceByName(String name) {
    return jdbcTemplate.query(GET_HEALTH_INSURANCE_BY_NAME, ROW_MAPPER, name).stream().findFirst();
  }
}
