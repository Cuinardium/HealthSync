package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.persistence.utils.QueryBuilder;
import ar.edu.itba.paw.persistence.utils.UpdateBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDaoImpl implements ImageDao {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert imageInsert;

  private static final String TABLE_NAME = "profile_picture";
  private static final String TABLE_PKEY_COLUMN = "profile_picture_id";
  private static final String TABLE_BYTEA_COLUMN = "profile_picture";

  @Autowired
  public ImageDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
    this.imageInsert =
        new SimpleJdbcInsert(ds)
            .withTableName(TABLE_NAME)
            .usingGeneratedKeyColumns(TABLE_PKEY_COLUMN);
  }

  // =============== Inserts ===============

  @Override
  public Image createImage(Image image) {
    // TODO: throw IllegalArgument?
    if (image == null) {
      return null;
    }
    Map<String, Object> data = new HashMap<>();
    data.put(TABLE_BYTEA_COLUMN, image.getBytes());
    final Number key = imageInsert.executeAndReturnKey(data);
    return getImage(key.longValue()).orElseThrow(IllegalStateException::new);
  }

  // =============== Updates ===============

  @Override
  public Image updateImage(Long pfpId, Image image) {
    // TODO: throw IllegalArgument?
    if (pfpId == null || image == null) {
      return null;
    }

    String updateQuery =
        new UpdateBuilder()
            .update(TABLE_NAME)
            .set(TABLE_BYTEA_COLUMN, "'\\x" + getHexString(image.getBytes()) + "'")
            .where(TABLE_PKEY_COLUMN + " = " + pfpId)
            .build();

    jdbcTemplate.update(updateQuery);
    return getImage(pfpId).orElseThrow(IllegalStateException::new);
  }
  
  // =============== Queries ===============

  @Override
  public Optional<Image> getImage(long id) {
    String query =
        new QueryBuilder()
            .select(TABLE_BYTEA_COLUMN)
            .from(TABLE_NAME)
            .where(TABLE_PKEY_COLUMN + " = " + id)
            .build();
    return jdbcTemplate.query(query, RowMappers.IMAGE_MAPPER).stream().findFirst();
  }

  // =============== Private ===============

  private String getHexString(byte[] bytea) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytea) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
