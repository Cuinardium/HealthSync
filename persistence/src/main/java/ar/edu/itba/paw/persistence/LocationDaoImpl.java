package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.LocationDao;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Location;
import java.sql.ResultSet;
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
      (rs, rowNum) -> {
        City city = City.values()[rs.getInt("city_code")];

        return new Location(rs.getLong("doctor_location_id"), city, rs.getString("address"));
      };

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert jdbcInsert;

  @Autowired
  public LocationDaoImpl(DataSource ds) {
    jdbcTemplate = new JdbcTemplate(ds);

    jdbcInsert =
        new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("doctor_location")
            .usingGeneratedKeyColumns("doctor_location_id");
  }

  @Override
  public Location createLocation(int cityCode, String address) {

    Map<String, Object> args = new HashMap<>();

    args.put("city_code", cityCode);
    args.put("address", address);

    final Number id = jdbcInsert.executeAndReturnKey(args);
    return new Location(id.longValue(), City.getCity(cityCode), address);
  }

  @Override
  public Optional<Location> getLocationById(long id) {
    String query =
        new QueryBuilder()
            .select()
            .from("doctor_location")
            .where("doctor_location_id = " + id)
            .build();

    return jdbcTemplate.query(query, ROW_MAPPER).stream().findFirst();
  }

  // Get all city codes present in the database
  @Override
  public Map<Integer, Integer> getUsedCities() {
    String query =
        new QueryBuilder()
            .select("city_code")
            .select("count(*) as qtycities")
            .from("doctor_location")
            .groupBy("city_code")
            .build();

    return jdbcTemplate.query(
        query,
        (ResultSet rs) -> {
          Map<Integer, Integer> results = new HashMap<>();
          while (rs.next()) {
            results.put(rs.getInt("city_code"), rs.getInt("qtycities"));
          }
          return results;
        });
  }
}
