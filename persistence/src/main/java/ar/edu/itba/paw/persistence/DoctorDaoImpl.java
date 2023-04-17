package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.models.Doctor;

import java.util.ArrayList;
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
      (rs, rowNum) ->
          new Doctor(
              rs.getLong("medic_id"),
              rs.getString("email"),
              rs.getString("password"),
              rs.getString("first_name"),
              rs.getString("last_name"),
              rs.getLong("profile_picture_id"),
              rs.getString("health_insurance_name"),
              rs.getString("medical_specialty_name"),
              rs.getString("medic_location_city"),
              rs.getString("medic_location_address"));

  /*
    SELECT medic.medic_id, email, password, first_name, last_name, profile_picture_id, health_insurance_name, medical_specialty_name, medic_location_city, medic_location_address
      FROM medic
      INNER JOIN users ON medic.user_id = users.user_id
      INNER JOIN medical_specialty ON medic.medical_specialty_id = medical_specialty.medical_specialty_id
      INNER JOIN medic_location_for_medic ON medic.medic_id = medic_location_for_medic.medic_id
      INNER JOIN medic_location ON medic_location_for_medic.medic_location_id = medic_location.medic_location_id
      INNER JOIN health_insurance_accepted_by_medic ON medic.medic_id = health_insurance_accepted_by_medic.medic_id
      INNER JOIN health_insurance ON health_insurance_accepted_by_medic.health_insurance_id = health_insurance.health_insurance_id;
  */

  // Joins medic, users, medical_specialty, medic_location_for_medic, medic_location,
  // health_insurance_accepted_by_medic, health_insurance
  private static final String GET_DOCTORS =
      "SELECT medic.medic_id, email, password, first_name, last_name, profile_picture_id,"
          + " health_insurance_name, medical_specialty_name, medic_location_city,"
          + " medic_location_address FROM medic INNER JOIN users ON medic.user_id = users.user_id"
          + " INNER JOIN medical_specialty ON medic.medical_specialty_id ="
          + " medical_specialty.medical_specialty_id INNER JOIN medic_location_for_medic ON"
          + " medic.medic_id = medic_location_for_medic.medic_id INNER JOIN medic_location ON"
          + " medic_location_for_medic.medic_location_id = medic_location.medic_location_id INNER"
          + " JOIN health_insurance_accepted_by_medic ON medic.medic_id ="
          + " health_insurance_accepted_by_medic.medic_id INNER JOIN health_insurance ON"
          + " health_insurance_accepted_by_medic.health_insurance_id ="
          + " health_insurance.health_insurance_id";

  private static final String GET_DOCTOR_BY_ID = GET_DOCTORS + " " + "WHERE medic.medic_id = ?";

  private static final String MATCHES_PART_NAME = "CONCAT(first_name, ' ', last_name) ILIKE CONCAT(?, '%')";
  private static final String MATCHES_CITY = "medic_location_city = ?"; 
  private static final String MATCHES_SPECIALTY = "medical_specialty_name = ?";
  private static final String MATCHES_HEALTH_INSURANCE = "health_insurance_name = ?";

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert doctorInsert;
  private final SimpleJdbcInsert doctorLocationInsert;
  private final SimpleJdbcInsert doctorHealthInsuranceInsert;

  @Autowired
  public DoctorDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);

    this.doctorInsert =
        new SimpleJdbcInsert(ds).withTableName("medic").usingGeneratedKeyColumns("medic_id");

    this.doctorLocationInsert = new SimpleJdbcInsert(ds).withTableName("medic_location_for_medic");

    this.doctorHealthInsuranceInsert =
        new SimpleJdbcInsert(ds).withTableName("health_insurance_accepted_by_medic");
  }

  @Override
  public void addHealthInsurance(long doctorId, long healthInsuranceId) {

    Map<String, Object> data = new HashMap<>();

    data.put("medic_id", doctorId);
    data.put("health_insurance_id", healthInsuranceId);

    doctorHealthInsuranceInsert.execute(data);
  }

  @Override
  public void addLocation(long doctorId, long locationId) {

    Map<String, Object> data = new HashMap<>();

    data.put("medic_id", doctorId);
    data.put("medic_location_id", locationId);

    doctorLocationInsert.execute(data);
  }

  @Override
  public long createDoctor(long userId, long specialtyId) {

    Map<String, Object> data = new HashMap<>();

    data.put("user_id", userId);
    data.put("medical_specialty_id", specialtyId);

    final Number key = doctorInsert.executeAndReturnKey(data);

    return key.longValue();
  }

  @Override
  public Optional<Doctor> getDoctorById(long id) {
    return jdbcTemplate.query(GET_DOCTOR_BY_ID, DOCTOR_MAPPER, id).stream().findFirst();
  }

  @Override
  public List<Doctor> getFilteredDoctors(String name, String specialty, String city, String healthInsurance) {

    // If no filters are applied, return all doctors
    if (name==null && specialty == null && city == null && healthInsurance == null) {
      return getDoctors();
    }

    // Start building the query
    String sql = GET_DOCTORS + " WHERE";
    List<Object> params = new ArrayList<>();

    // Add the filters to the query, if it is the first filter, don't add AND
    if (name != null) {
      sql += (params.isEmpty()? " " : " AND ") + MATCHES_PART_NAME;
      params.add(name);
    }

    if (specialty != null) {
      sql += (params.isEmpty()? " " : " AND ") + MATCHES_SPECIALTY;
      params.add(specialty);
    }

    if (city != null) {
      sql += (params.isEmpty()? " " : " AND ") + MATCHES_CITY;
      params.add(city);
    }

    if (healthInsurance != null) {
      sql += (params.isEmpty()? " " : " AND ") + MATCHES_HEALTH_INSURANCE;
      params.add(healthInsurance);
    }

    return jdbcTemplate.query(sql, DOCTOR_MAPPER, params.toArray());
  }

  @Override
  public List<Doctor> getDoctors() {
    return jdbcTemplate.query(GET_DOCTORS, DOCTOR_MAPPER);
  }
}
