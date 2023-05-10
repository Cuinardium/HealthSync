package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Specialty;
import java.sql.ResultSet;
import java.time.DayOfWeek;
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
        Location location =
            new Location(rs.getLong("doctor_location_id"), city, rs.getString("address"));

        AttendingHours attendingHours =
            new AttendingHours(
                rs.getLong("monday"),
                rs.getLong("tuesday"),
                rs.getLong("wednesday"),
                rs.getLong("thursday"),
                rs.getLong("friday"),
                rs.getLong("saturday"),
                rs.getLong("sunday"));

        return new Doctor(
            rs.getLong("doctor_id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getLong("profile_picture_id"),
            healthInsurance,
            specialty,
            location,
            attendingHours);
      };

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert doctorInsert;
  private final SimpleJdbcInsert doctorLocationInsert;
  private final SimpleJdbcInsert doctorHealthInsuranceInsert;
  private final SimpleJdbcInsert doctorAttendingHoursInsert;

  @Autowired
  public DoctorDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);

    this.doctorInsert = new SimpleJdbcInsert(ds).withTableName("doctor");

    this.doctorLocationInsert = new SimpleJdbcInsert(ds).withTableName("location_for_doctor");

    this.doctorHealthInsuranceInsert =
        new SimpleJdbcInsert(ds).withTableName("health_insurance_accepted_by_doctor");

    this.doctorAttendingHoursInsert =
        new SimpleJdbcInsert(ds)
            .withTableName("doctor_attending_hours")
            .usingGeneratedKeyColumns("attending_hours_id");
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
  public long createDoctor(long userId, int specialtyCode, AttendingHours attendingHours) {

    Map<String, Object> attendingHoursData = new HashMap<>();

    attendingHoursData.put(
        "monday", attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.MONDAY));
    attendingHoursData.put(
        "tuesday", attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.TUESDAY));
    attendingHoursData.put(
        "wednesday", attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.WEDNESDAY));
    attendingHoursData.put(
        "thursday", attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.THURSDAY));
    attendingHoursData.put(
        "friday", attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.FRIDAY));
    attendingHoursData.put(
        "saturday", attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.SATURDAY));
    attendingHoursData.put(
        "sunday", attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.SUNDAY));

    long attendingHoursId =
        doctorAttendingHoursInsert.executeAndReturnKey(attendingHoursData).longValue();

    Map<String, Object> doctorData = new HashMap<>();

    doctorData.put("doctor_id", userId);
    doctorData.put("specialty_code", specialtyCode);
    doctorData.put("attending_hours_id", attendingHoursId);

    doctorInsert.execute(doctorData);

    return userId;
  }

  // ======================== Updates =========================================

  @Override
  public void updateInformation(
      long doctorId, int healthInsuranceCode, int specialtyCode, int cityCode, String address) {
    String update =
        new UpdateBuilder()
            .update("doctor")
            .set("specialty_code", "'" + specialtyCode + "'")
            .where("doctor_id = (" + doctorId + ")")
            .build();

    jdbcTemplate.update(update);

    update =
        new UpdateBuilder()
            .update("health_insurance_accepted_by_doctor")
            .set("health_insurance_code", "'" + healthInsuranceCode + "'")
            .where("doctor_id = (" + doctorId + ")")
            .build();

    jdbcTemplate.update(update);

    String doctorLocationIdQuery =
        new QueryBuilder()
            .select("doctor_location_id")
            .from("location_for_doctor")
            .where("doctor_id = " + doctorId)
            .build();

    update =
        new UpdateBuilder()
            .update("doctor_location")
            .set("address", "'" + address + "'")
            .set("city_code", "'" + cityCode + "'")
            .where("doctor_location_id = (" + doctorLocationIdQuery + ")")
            .build();

    jdbcTemplate.update(update);
  }

  @Override
  public void updateAttendingHours(long doctorId, AttendingHours attendingHours) {

    String attendingHoursIdQuery =
        new QueryBuilder()
            .select("attending_hours_id")
            .from("doctor")
            .where("doctor_id = " + doctorId)
            .build();

    String update =
        new UpdateBuilder()
            .update("doctor_attending_hours")
            .set(
                "monday",
                "'" + attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.MONDAY) + "'")
            .set(
                "tuesday",
                "'" + attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.TUESDAY) + "'")
            .set(
                "wednesday",
                "'" + attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.WEDNESDAY) + "'")
            .set(
                "thursday",
                "'" + attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.THURSDAY) + "'")
            .set(
                "friday",
                "'" + attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.FRIDAY) + "'")
            .set(
                "saturday",
                "'" + attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.SATURDAY) + "'")
            .set(
                "sunday",
                "'" + attendingHours.getAttendingBlocksForDayAsBits(DayOfWeek.SUNDAY) + "'")
            .where("attending_hours_id = (" + attendingHoursIdQuery + ")")
            .build();

    jdbcTemplate.update(update);
  }

  // ============================ Queries =============================================

  @Override
  public Optional<Doctor> getDoctorById(long id) {

    String query = doctorQuery().where("doctor.doctor_id = " + id).build();

    return jdbcTemplate.query(query, DOCTOR_MAPPER).stream().findFirst();
  }

  @Override
  public List<Doctor> getFilteredDoctors(
      String name, int specialtyCode, int cityCode, int healthInsuranceCode) {

    // Start building the query
    QueryBuilder query = doctorQuery();

    // Add the filters to the query, if it is the first filter, don't add AND
    if (name != null && !name.isEmpty()) {
      query.where("CONCAT(first_name, ' ', last_name) ILIKE CONCAT('" + name + "', '%')");
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

  // Get used specialties and health insurances
  @Override
  public Map<Integer, Integer> getUsedHealthInsurances() {
    String query =
        new QueryBuilder()
            .select("health_insurance_code")
            .select("count(*) as qtyhealthinsurances")
            .from("health_insurance_accepted_by_doctor")
            .groupBy("health_insurance_code")
            .build();

    return jdbcTemplate.query(
        query,
        (ResultSet rs) -> {
          Map<Integer, Integer> result = new HashMap<>();
          while (rs.next()) {
            result.put(rs.getInt("health_insurance_code"), rs.getInt("qtyhealthinsurances"));
          }
          return result;
        });
  }

  @Override
  public Map<Integer, Integer> getUsedSpecialties() {
    String query =
        new QueryBuilder()
            .select("specialty_code")
            .select("count(*) as qtyspecialties")
            .from("doctor")
            .groupBy("specialty_code")
            .build();

    return jdbcTemplate.query(
        query,
        (ResultSet rs) -> {
          Map<Integer, Integer> result = new HashMap<>();
          while (rs.next()) {
            result.put(rs.getInt("specialty_code"), rs.getInt("qtyspecialties"));
          }
          return result;
        });
  }

  // ================================= Private ======================================
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
            "address",
            "monday",
            "tuesday",
            "wednesday",
            "thursday",
            "friday",
            "saturday",
            "sunday")
        .from("doctor")
        .innerJoin("users", "doctor_id = user_id")
        .innerJoin("location_for_doctor", "doctor.doctor_id = location_for_doctor.doctor_id")
        .innerJoin(
            "doctor_location",
            "location_for_doctor.doctor_location_id = doctor_location.doctor_location_id")
        .innerJoin(
            "health_insurance_accepted_by_doctor",
            "doctor.doctor_id = health_insurance_accepted_by_doctor.doctor_id")
        .innerJoin(
            "doctor_attending_hours",
            "doctor.attending_hours_id = doctor_attending_hours.attending_hours_id");
  }
}
