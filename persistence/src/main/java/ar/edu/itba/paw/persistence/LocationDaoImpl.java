package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.models.Location;
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
public class LocationDaoImpl implements LocationDao {

  private static final RowMapper<Location> ROW_MAPPER =
      (rs, rowNum) ->
          new Location(
              rs.getLong("medic_location_id"),
              rs.getString("medic_location_city"),
              rs.getString("medic_location_address"));

  private static final String GET_LOCATION_BY_ID =
      "SELECT * FROM medic_location WHERE medic_location_id = ?";

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert jdbcInsert;

  @Autowired
  public LocationDaoImpl(DataSource ds) {
    jdbcTemplate = new JdbcTemplate(ds);

    jdbcInsert =
        new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("medic_location")
            .usingGeneratedKeyColumns("medic_location_id");
  }

  @Override
  public Location createLocation(String city, String address) {

    Map<String, Object> args = new HashMap<>();

    args.put("medic_location_city", city);
    args.put("medic_location_address", address);

    Number id = jdbcInsert.executeAndReturnKey(args);

    return new Location(id.longValue(), city, address);
  }

  @Override
  public Optional<Location> getLocationById(long id) {
    return jdbcTemplate.query(GET_LOCATION_BY_ID, ROW_MAPPER, id).stream().findFirst();
  }
}
