package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.Image;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDaoImpl implements ImageDao {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public ImageDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
  }

  private static final RowMapper<Image> ROW_MAPPER =
      (rs, rowNum) -> new Image(rs.getBytes("profile_picture"));

  @Override
  public Optional<Image> getImage(long id) {
    String query =
        new QueryBuilder()
            .select("profile_picture")
            .from("profile_picture")
            .where("profile_picture_id = " + id)
            .build();
    return jdbcTemplate.query(query, ROW_MAPPER).stream().findFirst();
  }
}
