package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ReviewDao;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.persistence.utils.QueryBuilder;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewDaoImpl implements ReviewDao {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert reviewInsert;

  @Autowired
  public ReviewDaoImpl(DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
    this.reviewInsert =
        new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("review")
            .usingGeneratedKeyColumns("review_id");
  }

  // =============== Inserts ===============

  @Override
  public Review createReview(
      long doctorId, long patientId, int rating, LocalDate date, String description) {

    Map<String, Object> data = new HashMap<>();

    data.put("doctor_id", doctorId);
    data.put("patient_id", patientId);
    data.put("rating", rating);
    data.put("review_date", Date.valueOf(date));
    data.put("review_description", description);

    Number reviewId = reviewInsert.executeAndReturnKey(data);

    return getReviewById(reviewId.longValue()).orElseThrow(IllegalStateException::new);
  }

  // =============== Queries ===============

  @Override
  public Page<Review> getReviewsForDoctor(long doctorId, Integer page, Integer pageSize) {

    QueryBuilder query = reviewQuery().where("doctor_id = '" + doctorId + "'");
    String countQuery =
        new QueryBuilder()
            .select("count(*)")
            .from("review")
            .where("review.doctor_id = '" + doctorId + "'")
            .build();

    if (page != null && page >= 0 && pageSize != null && pageSize > 0) {
      query.limit(pageSize).offset(page * pageSize);
    }

    List<Review> reviews = jdbcTemplate.query(query.build(), RowMappers.REVIEW_MAPPER);
    Integer total = jdbcTemplate.queryForObject(countQuery, Integer.class);

    return new Page<>(reviews, page, total, pageSize);
  }

  // =============== Private ===============
  private Optional<Review> getReviewById(long reviewId) {
    String query = reviewQuery().where("review_id = '" + reviewId + "'").build();

    return jdbcTemplate.query(query, RowMappers.REVIEW_MAPPER).stream().findFirst();
  }

  private QueryBuilder reviewQuery() {
    return new QueryBuilder()
        .select(
            "review_id",
            "doctor_id",
            "review_date",
            "review_description",
            "rating",
            "review.patient_id",
            "email as patient_email",
            "password as patient_password",
            "first_name as patient_first_name",
            "last_name as patient_last_name",
            "profile_picture_id as patient_profile_picture_id",
            "health_insurance_code as patient_health_insurance_code")
        .from("review")
        .innerJoin("patient", "review.patient_id = patient.patient_id")
        .innerJoin("users", "patient.patient_id = users.user_id")
        .innerJoin(
            "health_insurance_for_patient",
            "patient.patient_id = health_insurance_for_patient.patient_id")
        .orderByDesc("review_date");
  }
}
