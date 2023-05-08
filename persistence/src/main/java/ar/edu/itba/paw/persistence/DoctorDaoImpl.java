package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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

  private final RowMapper<Doctor> doctorMapper = new DoctorMapper();
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
        "monday", attendingHoursToBits(attendingHours.getAttendingBlocksMonday()));
    attendingHoursData.put(
        "tuesday", attendingHoursToBits(attendingHours.getAttendingBlocksTuesday()));
    attendingHoursData.put(
        "wednesday", attendingHoursToBits(attendingHours.getAttendingBlocksWednesday()));
    attendingHoursData.put(
        "thursday", attendingHoursToBits(attendingHours.getAttendingBlocksThursday()));
    attendingHoursData.put(
        "friday", attendingHoursToBits(attendingHours.getAttendingBlocksFriday()));
    attendingHoursData.put(
        "saturday", attendingHoursToBits(attendingHours.getAttendingBlocksSaturday()));
    attendingHoursData.put(
        "sunday", attendingHoursToBits(attendingHours.getAttendingBlocksSunday()));

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
                "'" + attendingHoursToBits(attendingHours.getAttendingBlocksMonday()) + "'")
            .set(
                "tuesday",
                "'" + attendingHoursToBits(attendingHours.getAttendingBlocksTuesday()) + "'")
            .set(
                "wednesday",
                "'" + attendingHoursToBits(attendingHours.getAttendingBlocksWednesday()) + "'")
            .set(
                "thursday",
                "'" + attendingHoursToBits(attendingHours.getAttendingBlocksThursday()) + "'")
            .set(
                "friday",
                "'" + attendingHoursToBits(attendingHours.getAttendingBlocksFriday()) + "'")
            .set(
                "saturday",
                "'" + attendingHoursToBits(attendingHours.getAttendingBlocksSaturday()) + "'")
            .set(
                "sunday",
                "'" + attendingHoursToBits(attendingHours.getAttendingBlocksSunday()) + "'")
            .where("attending_hours_id = (" + attendingHoursIdQuery + ")")
            .build();

    jdbcTemplate.update(update);
  }

  // ============================ Queries =============================================

  @Override
  public Optional<Doctor> getDoctorById(long id) {

    String query = doctorQuery().where("doctor.doctor_id = " + id).build();

    return jdbcTemplate.query(query, doctorMapper).stream().findFirst();
  }

  @Override
  public Page<Doctor> getFilteredDoctors(
      String name,
      int specialtyCode,
      int cityCode,
      int healthInsuranceCode,
      int page,
      int pageSize) {

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

    if (page >= 0 && pageSize > 0) {
      query.limit(pageSize).offset(page * pageSize);
    }

    List<Doctor> doctors = jdbcTemplate.query(query.build(), doctorMapper);

    // Get the total number of doctors
    String doctorCountQuery = new QueryBuilder().select("count(*)").from("doctor").build();

    int totalDoctors = jdbcTemplate.queryForObject(doctorCountQuery, Integer.class);

    return new Page<>(doctors, page, totalDoctors); 
  }

  @Override
  public List<Doctor> getDoctors() {
    return jdbcTemplate.query(doctorQuery().build(), doctorMapper);
  }

  // Get used specialties and health insurances
  @Override
  public List<Integer> getUsedHealthInsurances() {
    String query =
        new QueryBuilder()
            .select("health_insurance_code")
            .distinct()
            .from("health_insurance_accepted_by_doctor")
            .build();

    return jdbcTemplate.queryForList(query, Integer.class);
  }

  @Override
  public List<Integer> getUsedSpecialties() {
    String query = new QueryBuilder().select("specialty_code").distinct().from("doctor").build();

    return jdbcTemplate.queryForList(query, Integer.class);
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

  private long attendingHoursToBits(Collection<ThirtyMinuteBlock> attendingHours) {

    long bits = 0;

    for (ThirtyMinuteBlock block : attendingHours) {
      bits |= (1L << block.ordinal());
    }

    return bits;
  }

  private class DoctorMapper implements RowMapper<Doctor> {

    // Most significant 16 bits dont encode anything
    // The 48 least significant bits encode 30 minute blocks
    // If the bit is 1 the doctor is available, otherwise it is not
    // The least significant bit encodes the (0:00, 0:30) block
    // The 48th bit encodes the (23:3, 0:00) block
    private Collection<ThirtyMinuteBlock> attendingHoursFromBits(long bits) {

      Collection<ThirtyMinuteBlock> blocks = new ArrayList<>();
      ThirtyMinuteBlock[] values = ThirtyMinuteBlock.values();

      for (int i = 0; i < 48; i++) {
        if ((bits & (1L << i)) != 0) {
          blocks.add(values[i]);
        }
      }

      return blocks;
    }

    @Override
    public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
      Specialty specialty = Specialty.values()[rs.getInt("specialty_code")];
      HealthInsurance healthInsurance =
          HealthInsurance.values()[rs.getInt("health_insurance_code")];

      City city = City.values()[rs.getInt("city_code")];
      Location location =
          new Location(rs.getLong("doctor_location_id"), city, rs.getString("address"));

      AttendingHours attendingHours =
          new AttendingHours(
              attendingHoursFromBits(rs.getLong("monday")),
              attendingHoursFromBits(rs.getLong("tuesday")),
              attendingHoursFromBits(rs.getLong("wednesday")),
              attendingHoursFromBits(rs.getLong("thursday")),
              attendingHoursFromBits(rs.getLong("friday")),
              attendingHoursFromBits(rs.getLong("saturday")),
              attendingHoursFromBits(rs.getLong("sunday")));

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
    }
  }
}
