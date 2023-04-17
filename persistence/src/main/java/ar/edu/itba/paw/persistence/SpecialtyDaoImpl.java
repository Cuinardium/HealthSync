package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.SpecialtyDao;
import ar.edu.itba.paw.models.Specialty;

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
public class SpecialtyDaoImpl implements SpecialtyDao {

  private static final String GET_SPECIALTY_BY_ID = "SELECT * FROM medical_specialty WHERE medical_specialty_id = ?";
  private static final String GET_SPECIALTY_BY_NAME = "SELECT * FROM medical_specialty WHERE medical_specialty_name = ?";

  private static final RowMapper<Specialty> ROW_MAPPER = (rs, rowNum) -> new Specialty(
      rs.getLong("medical_specialty_id"),
      rs.getString("medical_specialty_name")
  );



  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert jdbcInsert;

  @Autowired
  public SpecialtyDaoImpl(DataSource ds) {
    jdbcTemplate = new JdbcTemplate(ds);
    jdbcInsert =
        new SimpleJdbcInsert(ds)
            .withTableName("medical_specialty")
            .usingGeneratedKeyColumns("medical_specialty_id");
  }

  @Override
  public Specialty createSpecialty(String name) {

    Map<String, Object> args = new HashMap<>();

    args.put("medical_specialty_name", name);

    Number id = jdbcInsert.executeAndReturnKey(args);

    return new Specialty(id.longValue(), name);
  }

  @Override
  public Optional<Specialty> getSpecialtyById(long id) {
    return jdbcTemplate.query(GET_SPECIALTY_BY_ID, ROW_MAPPER, id).stream().findFirst();
  }

  @Override
  public Optional<Specialty> getSpecialtyByName(String name) {
    return jdbcTemplate.query(GET_SPECIALTY_BY_NAME, ROW_MAPPER, name).stream().findFirst();
  }
}
