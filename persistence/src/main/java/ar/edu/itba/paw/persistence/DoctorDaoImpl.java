package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.persistence.exceptions.DoctorAlreadyExistsException;
import ar.edu.itba.paw.persistence.exceptions.DoctorNotFoundException;
import ar.edu.itba.paw.persistence.utils.DeleteBuilder;
import ar.edu.itba.paw.persistence.utils.QueryBuilder;
import ar.edu.itba.paw.persistence.utils.UpdateBuilder;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorDaoImpl implements DoctorDao {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert doctorInsert;
  private final SimpleJdbcInsert locationInsert;
  private final SimpleJdbcInsert doctorLocationInsert;
  private final SimpleJdbcInsert doctorHealthInsuranceInsert;
  private final SimpleJdbcInsert doctorAttendingHoursInsert;

  @Autowired
  public DoctorDaoImpl(final DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);

    this.doctorInsert = new SimpleJdbcInsert(ds).withTableName("doctor");

    this.locationInsert =
        new SimpleJdbcInsert(ds)
            .withTableName("doctor_location")
            .usingGeneratedKeyColumns("doctor_location_id");

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
  public Doctor createDoctor(
      long userId,
      Specialty specialty,
      City city,
      String address,
      List<HealthInsurance> healthInsurances,
      AttendingHours attendingHours) {

    long attendingHoursId = createAttendingHours(attendingHours);

    // Create doctor
    Map<String, Object> doctorData = new HashMap<>();

    doctorData.put("doctor_id", userId);
    doctorData.put("specialty_code", specialty.ordinal());
    doctorData.put("attending_hours_id", attendingHoursId);

    try {
      doctorInsert.execute(doctorData);
    } catch (DuplicateKeyException e) {
      throw new DoctorAlreadyExistsException();
    }
    addLocation(userId, city.ordinal(), address);

    // Add health insurances
    for (HealthInsurance healthInsurance : healthInsurances) {
      addHealthInsurance(userId, healthInsurance.ordinal());
    }

    return getDoctorById(userId).orElseThrow(IllegalStateException::new);
  }

  // ======================== Updates =========================================

  @Override
  public Doctor updateDoctorInfo(
      long doctorId,
      Specialty specialty,
      City city,
      String address,
      List<HealthInsurance> healthInsurances,
      AttendingHours attendingHours) {

    String specialtyUpdate =
        new UpdateBuilder()
            .update("doctor")
            .set("specialty_code", "'" + specialty.ordinal() + "'")
            .where("doctor_id = (" + doctorId + ")")
            .build();

    jdbcTemplate.update(specialtyUpdate);

    updateLocation(doctorId, city.ordinal(), address);

    updateHealthInsurances(
        doctorId, healthInsurances.stream().map(Enum::ordinal).collect(Collectors.toList()));

    updateAttendingHours(doctorId, attendingHours);

    return getDoctorById(doctorId).orElseThrow(DoctorNotFoundException::new);
  }

  // ============================ Queries =============================================

  @Override
  public Optional<Doctor> getDoctorById(long id) {

    String query = doctorQuery().where("doctor.doctor_id = " + id).build();

    return jdbcTemplate.query(query, RowMappers.DOCTOR_EXTRACTOR).stream().findFirst();
  }

  @Override
  public Page<Doctor> getFilteredDoctors(
      String name,
      Specialty specialty,
      City city,
      HealthInsurance healthInsurance,
      int page,
      int pageSize) {

    int specialtyCode = specialty != null ? specialty.ordinal() : -1;
    int cityCode = city != null ? city.ordinal() : -1;
    int healthInsuranceCode = healthInsurance != null ? healthInsurance.ordinal() : -1;

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

    List<Doctor> doctors = jdbcTemplate.query(query, RowMappers.DOCTOR_EXTRACTOR);

    int totalDoctors = jdbcTemplate.queryForObject(doctorCountQuery.build(), Integer.class);

    return new Page<>(doctors, page, totalDoctors);
  }

  @Override
  public List<Doctor> getDoctors() {
    return jdbcTemplate.query(doctorQuery().build(), RowMappers.DOCTOR_EXTRACTOR);
  }

  // Get used specialties and health insurances
  @Override
  public Map<HealthInsurance, Integer> getUsedHealthInsurances() {
    String query =
        new QueryBuilder()
            .select("health_insurance_code")
            .select("count(*) as qtyhealthinsurances")
            .from("health_insurance_accepted_by_doctor")
            .groupBy("health_insurance_code")
            .build();

    Map<Integer, Integer> codeMap =
        jdbcTemplate.query(
            query,
            (ResultSet rs) -> {
              Map<Integer, Integer> result = new HashMap<>();
              while (rs.next()) {
                result.put(rs.getInt("health_insurance_code"), rs.getInt("qtyhealthinsurances"));
              }
              return result;
            });

    // Convert the code to the enum
    Map<HealthInsurance, Integer> result = new HashMap<>();

    codeMap.forEach(
        (code, qty) -> {
          HealthInsurance healthInsurance = HealthInsurance.values()[code];
          result.put(healthInsurance, qty);
        });

    return result;
  }

  @Override
  public Map<Specialty, Integer> getUsedSpecialties() {
    String query =
        new QueryBuilder()
            .select("specialty_code")
            .select("count(*) as qtyspecialties")
            .from("doctor")
            .groupBy("specialty_code")
            .build();

    Map<Integer, Integer> codeMap =
        jdbcTemplate.query(
            query,
            (ResultSet rs) -> {
              Map<Integer, Integer> result = new HashMap<>();
              while (rs.next()) {
                result.put(rs.getInt("specialty_code"), rs.getInt("qtyspecialties"));
              }
              return result;
            });

    // Convert the code to the enum
    Map<Specialty, Integer> result = new HashMap<>();

    codeMap.forEach(
        (code, qty) -> {
          Specialty specialty = Specialty.values()[code];
          result.put(specialty, qty);
        });

    return result;
  }

  // Get all city codes present in the database
  @Override
  public Map<City, Integer> getUsedCities() {
    String query =
        new QueryBuilder()
            .select("city_code")
            .select("count(*) as qtycities")
            .from("doctor_location")
            .groupBy("city_code")
            .build();

    Map<Integer, Integer> codeMap =
        jdbcTemplate.query(
            query,
            (ResultSet rs) -> {
              Map<Integer, Integer> results = new HashMap<>();
              while (rs.next()) {
                results.put(rs.getInt("city_code"), rs.getInt("qtycities"));
              }
              return results;
            });

    // Convert the code to the enum
    Map<City, Integer> result = new HashMap<>();

    codeMap.forEach(
        (code, qty) -> {
          City city = City.values()[code];
          result.put(city, qty);
        });

    return result;
  }

  // ================================= Private ======================================

  // ============ Health Insurance ========

  private void addHealthInsurance(long doctorId, int healthInsuranceCode) {

    Map<String, Object> data = new HashMap<>();

    data.put("doctor_id", doctorId);
    data.put("health_insurance_code", healthInsuranceCode);

    doctorHealthInsuranceInsert.execute(data);
  }

  private void updateHealthInsurances(long doctorId, List<Integer> healthInsuranceCodes) {

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
  }

  // ============ Location ==============

  private void addLocation(long doctorId, int cityCode, String address) {

    // Create the location
    Map<String, Object> data = new HashMap<>();

    data.put("city_code", cityCode);
    data.put("address", address);

    Number locationId = locationInsert.executeAndReturnKey(data);

    data.clear();

    data.put("doctor_id", doctorId);
    data.put("doctor_location_id", locationId);

    doctorLocationInsert.execute(data);
  }

  private void updateLocation(long doctorId, int cityCode, String address) {

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

  // ============ Attending Hours ========

  private long createAttendingHours(AttendingHours attendingHours) {

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

    return doctorAttendingHoursInsert.executeAndReturnKey(attendingHoursData).longValue();
  }

  private void updateAttendingHours(long doctorId, AttendingHours attendingHours) {

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
}
