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
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorDaoImpl implements DoctorDao {

  private final ResultSetExtractor<List<Doctor>> doctorExtractor = new DoctorExtractor();
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
        "monday",
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.MONDAY)));
    attendingHoursData.put(
        "tuesday",
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.TUESDAY)));
    attendingHoursData.put(
        "wednesday",
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.WEDNESDAY)));
    attendingHoursData.put(
        "thursday",
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.THURSDAY)));
    attendingHoursData.put(
        "friday",
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.FRIDAY)));
    attendingHoursData.put(
        "saturday",
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.SATURDAY)));
    attendingHoursData.put(
        "sunday",
        ThirtyMinuteBlock.toBits(attendingHours.getAttendingBlocksForDay(DayOfWeek.SUNDAY)));

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
  public void updateDoctorInfo(
      long doctorId,
      List<Integer> healthInsuranceCodes,
      int specialtyCode,
      int cityCode,
      String address) {

    String specialtyUpdate =
        new UpdateBuilder()
            .update("doctor")
            .set("specialty_code", "'" + specialtyCode + "'")
            .where("doctor_id = (" + doctorId + ")")
            .build();

    jdbcTemplate.update(specialtyUpdate);

    // Get existing health insurance codes
    String existingHealthInsuranceCodesQuery =
        new QueryBuilder()
            .select("health_insurance_code")
            .from("health_insurance_accepted_by_doctor")
            .where("doctor_id = " + doctorId)
            .build();

    List<Integer> existingHealthInsuranceCodes =
        jdbcTemplate.queryForList(existingHealthInsuranceCodesQuery, Integer.class);

    // Remove health insurance codes that are not in the new list
    String deleteHealthInsurances =
        new DeleteBuilder()
            .delete("health_insurance_accepted_by_doctor")
            .where("doctor_id = " + doctorId)
            .where("health_insurance_code = ?")
            .build();

    for (Integer code : existingHealthInsuranceCodes) {
      if (!healthInsuranceCodes.contains(code)) {
        jdbcTemplate.update(deleteHealthInsurances, code);
      }
    }

    // Add health insurance codes that are not in the old list
    for (Integer code : healthInsuranceCodes) {
      if (!existingHealthInsuranceCodes.contains(code)) {
        addHealthInsurance(doctorId, code);
      }
    }

    String doctorLocationIdQuery =
        new QueryBuilder()
            .select("doctor_location_id")
            .from("location_for_doctor")
            .where("doctor_id = " + doctorId)
            .build();

    String doctorLocationUpdate =
        new UpdateBuilder()
            .update("doctor_location")
            .set("address", "'" + address + "'")
            .set("city_code", "'" + cityCode + "'")
            .where("doctor_location_id = (" + doctorLocationIdQuery + ")")
            .build();

    jdbcTemplate.update(doctorLocationUpdate);
  }

  @Override
  public void updateDoctorAttendingHours(long doctorId, AttendingHours attendingHours) {

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
                "'"
                    + ThirtyMinuteBlock.toBits(
                        attendingHours.getAttendingBlocksForDay(DayOfWeek.MONDAY))
                    + "'")
            .set(
                "tuesday",
                "'"
                    + ThirtyMinuteBlock.toBits(
                        attendingHours.getAttendingBlocksForDay(DayOfWeek.TUESDAY))
                    + "'")
            .set(
                "wednesday",
                "'"
                    + ThirtyMinuteBlock.toBits(
                        attendingHours.getAttendingBlocksForDay(DayOfWeek.WEDNESDAY))
                    + "'")
            .set(
                "thursday",
                "'"
                    + ThirtyMinuteBlock.toBits(
                        attendingHours.getAttendingBlocksForDay(DayOfWeek.THURSDAY))
                    + "'")
            .set(
                "friday",
                "'"
                    + ThirtyMinuteBlock.toBits(
                        attendingHours.getAttendingBlocksForDay(DayOfWeek.FRIDAY))
                    + "'")
            .set(
                "saturday",
                "'"
                    + ThirtyMinuteBlock.toBits(
                        attendingHours.getAttendingBlocksForDay(DayOfWeek.SATURDAY))
                    + "'")
            .set(
                "sunday",
                "'"
                    + ThirtyMinuteBlock.toBits(
                        attendingHours.getAttendingBlocksForDay(DayOfWeek.SUNDAY))
                    + "'")
            .where("attending_hours_id = (" + attendingHoursIdQuery + ")")
            .build();

    jdbcTemplate.update(update);
  }

  // ============================ Queries =============================================

  @Override
  public Optional<Doctor> getDoctorById(long id) {

    String query = doctorQuery().where("doctor.doctor_id = " + id).build();

    return jdbcTemplate.query(query, doctorExtractor).stream().findFirst();
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
    QueryBuilder subQuery = doctorQuery().distinctOn("doctor.doctor_id");
    QueryBuilder doctorCountQuery = doctorCountQuery();

    // Add the filters to the query, if it is the first filter, don't add AND
    if (name != null && !name.isEmpty()) {
      subQuery.where("CONCAT(first_name, ' ', last_name) ILIKE CONCAT('" + name + "', '%')");
      doctorCountQuery.where(
          "CONCAT(first_name, ' ', last_name) ILIKE CONCAT('" + name + "', '%')");
    }

    if (specialtyCode >= 0) {
      subQuery.where("specialty_code = " + specialtyCode);
      doctorCountQuery.where("specialty_code = " + specialtyCode);
    }

    if (cityCode >= 0) {
      subQuery.where("city_code = " + cityCode);
      doctorCountQuery.where("city_code = " + cityCode);
    }

    if (healthInsuranceCode >= 0) {
      String healthInsuranceQuery =
          new QueryBuilder()
              .select("doctor_id")
              .from("health_insurance_accepted_by_doctor")
              .where("health_insurance_code = " + healthInsuranceCode)
              .build();

      subQuery.where("doctor.doctor_id IN (" + healthInsuranceQuery + ")");
      doctorCountQuery.where("doctor.doctor_id IN (" + healthInsuranceQuery + ")");
    }

    if (page >= 0 && pageSize > 0) {
      subQuery.limit(pageSize).offset(page * pageSize);
    }

    // Outer query
    String query =
        new QueryBuilder()
            .select(
                "subquery.doctor_id",
                "specialty_code",
                "email",
                "password",
                "first_name",
                "last_name",
                "profile_picture_id",
                "health_insurance_accepted_by_doctor.health_insurance_code",
                "doctor_location_id",
                "city_code",
                "address",
                "monday",
                "tuesday",
                "wednesday",
                "thursday",
                "friday",
                "saturday",
                "sunday")
            .from("(" + subQuery.build() + ") as subquery")
            .leftJoin(
                "health_insurance_accepted_by_doctor",
                "subquery.doctor_id = health_insurance_accepted_by_doctor.doctor_id")
            .build();

    List<Doctor> doctors = jdbcTemplate.query(query, doctorExtractor);

    int totalDoctors = jdbcTemplate.queryForObject(doctorCountQuery.build(), Integer.class);

    return new Page<>(doctors, page, totalDoctors);
  }

  @Override
  public List<Doctor> getDoctors() {
    return jdbcTemplate.query(doctorQuery().build(), doctorExtractor);
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

  private QueryBuilder doctorCountQuery() {
    return new QueryBuilder()
        .select("count(distinct doctor.doctor_id)")
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

  private class DoctorExtractor implements ResultSetExtractor<List<Doctor>> {

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
    public List<Doctor> extractData(ResultSet rs) throws SQLException, DataAccessException {
      Map<Long, Doctor> doctors = new HashMap<>();
      Specialty[] specialties = Specialty.values();
      City[] cities = City.values();
      HealthInsurance[] healthInsurances = HealthInsurance.values();

      while (rs.next()) {
        long doctorId = rs.getLong("doctor_id");
        Doctor doctor = doctors.get(doctorId);

        if (doctor == null) {
          Specialty specialty = specialties[rs.getInt("specialty_code")];

          City city = cities[rs.getInt("city_code")];
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

          doctor =
              new Doctor(
                  rs.getLong("doctor_id"),
                  rs.getString("email"),
                  rs.getString("password"),
                  rs.getString("first_name"),
                  rs.getString("last_name"),
                  rs.getObject("profile_picture_id") == null
                      ? null
                      : rs.getLong("profile_picture_id"),
                  new ArrayList<>(),
                  specialty,
                  location,
                  attendingHours);

          doctors.put(doctorId, doctor);
        }

        doctor.getHealthInsurances().add(healthInsurances[rs.getInt("health_insurance_code")]);
      }

      return new ArrayList<>(doctors.values());
    }
  }
}
