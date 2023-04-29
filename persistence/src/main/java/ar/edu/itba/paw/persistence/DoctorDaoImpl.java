package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Specialty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorDaoImpl implements DoctorDao {

  private static final RowMapper<Doctor> DOCTOR_MAPPER =
      (rs, rowNum) -> {
        Specialty specialty = Specialty.values()[rs.getInt("specialty_code")];
        HealthInsurance healthInsurance =
            HealthInsurance.values()[rs.getInt("health_insurance_code")];

        City city = City.values()[rs.getInt("city_code")];
        Location location = new Location(rs.getLong("doctor_location_id"), city, rs.getString("address"));

        return new Doctor(
            rs.getLong("doctor_id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getLong("profile_picture_id"),
            healthInsurance,
            specialty,
            location);
      };

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert doctorInsert;
  private final SimpleJdbcInsert doctorLocationInsert;
  private final SimpleJdbcInsert doctorHealthInsuranceInsert;

  @Autowired
  public DoctorDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);

    this.doctorInsert = new SimpleJdbcInsert(ds).withTableName("doctor");

    this.doctorLocationInsert = new SimpleJdbcInsert(ds).withTableName("location_for_doctor");

    this.doctorHealthInsuranceInsert =
        new SimpleJdbcInsert(ds).withTableName("health_insurance_accepted_by_doctor");
  }

  // ======================== Inserts =========================================

  @Override
  public void addHealthInsurance(long doctorId, int healthInsuranceCode) {

    Map<String, Object> data = new HashMap<>();

    data.put("doctor_id", doctorId);
    data.put("health_insurance_code", healthInsuranceCode);

    doctorHealthInsuranceInsert.execute(data);
  }

  @Override
  public void addLocation(long doctorId, long locationId) {

    Map<String, Object> data = new HashMap<>();

    data.put("doctor_id", doctorId);
    data.put("doctor_location_id", locationId);

    doctorLocationInsert.execute(data);
  }

  @Override
  public long createDoctor(long userId, int specialtyCode) {

    Map<String, Object> data = new HashMap<>();

    data.put("doctor_id", userId);
    data.put("specialty_code", specialtyCode);

    doctorInsert.execute(data);

    return userId;
  }

  // ============================ Queries =============================================

  @Override
  public Optional<Doctor> getDoctorById(long id) {

    String query = doctorQuery().where("medic.medic_id = " + id).build();

    return jdbcTemplate.query(query, DOCTOR_MAPPER).stream().findFirst();
  }

  @Override
  public List<Doctor> getFilteredDoctors(
      String name, int specialtyCode, int cityCode, int healthInsuranceCode) {

    // Start building the query
    QueryBuilder query = doctorQuery();

    // Add the filters to the query, if it is the first filter, don't add AND
    if (name != null) {
      query.where("CONCAT(first_name, ' ', last_name) ILIKE CONCAT(" + name + ", '%')");
    }

    if (specialtyCode >= 0) {
      query.where("specialty_code = " + specialtyCode);
    }

    if (cityCode >= 0) {
      query.where("city_code = " + cityCode);
    }

    if (healthInsuranceCode >= 0) {
      query.where("health_insurance_code = " + healthInsuranceCode);
    }

    return jdbcTemplate.query(query.build(), DOCTOR_MAPPER);
  }

  @Override
  public List<Doctor> getDoctors() {
    return jdbcTemplate.query(doctorQuery().build(), DOCTOR_MAPPER);
  }

  // ================================= Private ======================================

  // Joins medic, users, medical_specialty, medic_location_for_medic, medic_location,
  // health_insurance_accepted_by_medic, health_insurance
  private QueryBuilder doctorQuery() {
    return new QueryBuilder()
        .select(
            "doctor.doctor_id",
            "specialty_code",
            "email",
            "password",
            "first_name",
            "last_name",
            "profile_picture_id",
            "health_insurance_code",
            "doctor_location.doctor_location_id",
            "city_code",
            "address")
        .from("doctor")
        .innerJoin("users", "doctor_id = user_id")
        .innerJoin("location_for_doctor", "doctor.doctor_id = location_for_doctor.doctor_id")
        .innerJoin(
            "doctor_location",
            "location_for_doctor.doctor_location_id = doctor_location.doctor_location_id")
        .innerJoin(
            "health_insurance_accepted_by_doctor",
            "doctor.doctor_id = health_insurance_accepted_by_doctor.doctor_id");
  }
}
