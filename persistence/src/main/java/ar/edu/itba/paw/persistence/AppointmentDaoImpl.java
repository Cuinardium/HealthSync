package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
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

        return new Appointment(
            appointmentId, patientId, doctorId, date, timeBlock, status, description);
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
        description);
  }

  @Override
  public void updateAppointmentStatus(long appointmentId, AppointmentStatus status) {
    String update =
        new UpdateBuilder()
            .update("appointment")
            .set("status_code", Integer.toString(status.ordinal()))
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
  public List<Appointment> getAppointmentsForPatient(long patientId) {
    String query =
        new QueryBuilder()
            .select("*")
            .from("appointment")
            .where("patient_id = " + patientId)
            .build();

    return jdbcTemplate.query(query, APPOINTMENT_MAPPER);
  }

  @Override
  public List<Appointment> getAppointmentsForDoctor(long doctorId) {
    String query =
        new QueryBuilder().select("*").from("appointment").where("doctor_id = " + doctorId).build();

    return jdbcTemplate.query(query, APPOINTMENT_MAPPER);
  }

  @Override
  public List<Appointment> getAppointmentsForDoctorByStatus(
      long doctorId, AppointmentStatus status) {
    String query =
        new QueryBuilder()
            .select("*")
            .from("appointment")
            .where("doctor_id = " + doctorId)
            .where("status_code = " + status.ordinal())
            .build();

    return jdbcTemplate.query(query, APPOINTMENT_MAPPER);
  }

  @Override
  public List<Appointment> getAppointmentsForPatientByStatus(
      long patientId, AppointmentStatus status) {
    String query =
        new QueryBuilder()
            .select("*")
            .from("appointment")
            .where("patient_id = " + patientId)
            .where("status_code = " + status.ordinal())
            .build();

    return jdbcTemplate.query(query, APPOINTMENT_MAPPER);
  }

  // ========================== Private ==========================
  private short timeBlockToSmallInt(ThirtyMinuteBlock timeBlock) {
    return (short) timeBlock.ordinal();
  }
}
