package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import java.sql.Date;
import java.time.LocalDate;
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
public class AppointmentDaoImpl implements AppointmentDao {

  private static final RowMapper<Appointment> APPOINTMENT_MAPPER =
      (rs, rowNum) -> {
        long appointmentId = rs.getLong("appointment_id");
        long patientId = rs.getLong("patient_id");
        long doctorId = rs.getLong("doctor_id");
        LocalDate date = rs.getDate("appointment_date").toLocalDate();
        ThirtyMinuteBlock timeBlock = ThirtyMinuteBlock.values()[rs.getShort("appointment_time")];
        AppointmentStatus status = AppointmentStatus.values()[rs.getInt("status_code")];
        String description = rs.getString("appointment_description");
        String cancelDesc = rs.getString("cancel_description");
        return new Appointment(
            appointmentId, patientId, doctorId, date, timeBlock, status, description, cancelDesc);
      };

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert appointmentInsert;

  @Autowired
  public AppointmentDaoImpl(DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
    this.appointmentInsert =
        new SimpleJdbcInsert(ds)
            .withTableName("appointment")
            .usingGeneratedKeyColumns("appointment_id");
  }

  // ========================== Inserts ==========================
  @Override
  public Appointment createAppointment(
      long patientId,
      long doctorId,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description) {

    Map<String, Object> data = new HashMap<>();

    data.put("patient_id", patientId);
    data.put("doctor_id", doctorId);
    data.put("appointment_date", Date.valueOf(date));
    data.put("appointment_time", timeBlockToSmallInt(timeBlock));
    data.put("appointment_description", description);
    data.put("status_code", AppointmentStatus.PENDING.ordinal());

    long appointmentId = appointmentInsert.executeAndReturnKey(data).longValue();

    return new Appointment(
        appointmentId,
        patientId,
        doctorId,
        date,
        timeBlock,
        AppointmentStatus.PENDING,
        description,
        null);
  }

  @Override
  public void updateAppointmentStatus(
      long appointmentId, AppointmentStatus status, String cancelDescription) {
    String update =
        new UpdateBuilder()
            .update("appointment")
            .set("status_code", Integer.toString(status.ordinal()))
            .set("cancel_description", "'" + cancelDescription + "'")
            .where("appointment_id = " + appointmentId)
            .build();

    jdbcTemplate.update(update);
  }

  // ========================== Queries ==========================
  @Override
  public Optional<Appointment> getAppointmentById(long appointmentId) {
    String query =
        new QueryBuilder()
            .select("*")
            .from("appointment")
            .where("appointment_id = " + appointmentId)
            .build();

    return jdbcTemplate.query(query, APPOINTMENT_MAPPER).stream().findFirst();
  }

  @Override
  public Optional<Appointment> getAppointment(
      long doctorId, LocalDate date, ThirtyMinuteBlock timeBlock) {
    String query =
        new QueryBuilder()
            .select("*")
            .from("appointment")
            .where("doctor_id = " + doctorId)
            .where("appointment_date = '" + Date.valueOf(date) + "'")
            .where("appointment_time = " + timeBlockToSmallInt(timeBlock))
            .build();

    return jdbcTemplate.query(query, APPOINTMENT_MAPPER).stream().findFirst();
  }

  @Override
  public List<Appointment> getAppointmentsForPatient(long patientId) {
    String query =
        new QueryBuilder()
            .select("*")
            .from("appointment")
            .where("patient_id = " + patientId)
            .orderByAsc("appointment_date")
            .orderByAsc("appointment_time")
            .build();

    return jdbcTemplate.query(query, APPOINTMENT_MAPPER);
  }

  @Override
  public List<Appointment> getAppointmentsForDoctor(long doctorId) {
    String query =
        new QueryBuilder()
            .select("*")
            .from("appointment")
            .where("doctor_id = " + doctorId)
            .orderByAsc("appointment_date")
            .orderByAsc("appointment_time")
            .build();

    return jdbcTemplate.query(query, APPOINTMENT_MAPPER);
  }

  @Override
  public Page<Appointment> getFilteredAppointmentsForDoctor(
      long doctorId,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      int page,
      int pageSize) {

    // Get the appointments for the doctor
    String appointmentsQuery =
        appointmentsQuery(doctorId, true, status, from, to, page, pageSize).build();

    List<Appointment> appointments = jdbcTemplate.query(appointmentsQuery, APPOINTMENT_MAPPER);

    // Get the total number of appointments for the doctor
    String appointmentsCountQuery =
        appointmentsCountQuery(doctorId, true, status, from, to).build();

    int totalAppointments = jdbcTemplate.queryForObject(appointmentsCountQuery, Integer.class);

    return new Page<>(appointments, page, totalAppointments);
  }

  @Override
  public Page<Appointment> getFilteredAppointmentsForPatient(
      long patientId,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      int page,
      int pageSize) {

    // Get the appointments for the patient
    String appointmentsQuery =
        appointmentsQuery(patientId, false, status, from, to, page, pageSize).build();

    List<Appointment> appointments = jdbcTemplate.query(appointmentsQuery, APPOINTMENT_MAPPER);

    // Get the total number of appointments for the patient
    String appointmentsCountQuery =
        appointmentsCountQuery(patientId, false, status, from, to).build();

    int totalAppointments = jdbcTemplate.queryForObject(appointmentsCountQuery, Integer.class);

    return new Page<>(appointments, page, totalAppointments);
  }

  // ========================== Private ==========================

  private QueryBuilder appointmentsQuery(
      long userId,
      boolean isDoctor,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      int page,
      int pageSize) {

    String userField = isDoctor ? "doctor_id" : "patient_id";

    QueryBuilder appointmentsQuery =
        new QueryBuilder().select("*").from("appointment").where(userField + " = " + userId);

    if (status != null) {
      appointmentsQuery.where("status_code = " + status.ordinal());
    }

    if (from != null) {
      appointmentsQuery.where("appointment_date >= '" + Date.valueOf(from) + "'");
    }

    if (to != null) {
      appointmentsQuery.where("appointment_date <= '" + Date.valueOf(to) + "'");
    }

    appointmentsQuery.orderByAsc("appointment_date").orderByAsc("appointment_time");

    if (page >= 0 && pageSize > 0) {
      appointmentsQuery.limit(pageSize).offset(page * pageSize);
    }

    return appointmentsQuery;
  }

  private QueryBuilder appointmentsCountQuery(
      long userId, boolean isDoctor, AppointmentStatus status, LocalDate from, LocalDate to) {

    String userField = isDoctor ? "doctor_id" : "patient_id";

    QueryBuilder appointmentsQuery =
        new QueryBuilder().select("count(*)").from("appointment").where(userField + " = " + userId);

    if (status != null) {
      appointmentsQuery.where("status_code = " + status.ordinal());
    }

    if (from != null) {
      appointmentsQuery.where("appointment_date >= '" + Date.valueOf(from) + "'");
    }

    if (to != null) {
      appointmentsQuery.where("appointment_date <= '" + Date.valueOf(to) + "'");
    }

    return appointmentsQuery;
  }

  private short timeBlockToSmallInt(ThirtyMinuteBlock timeBlock) {
    return (short) timeBlock.ordinal();
  }
}
