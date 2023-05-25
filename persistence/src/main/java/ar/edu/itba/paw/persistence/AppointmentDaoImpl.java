package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.persistence.exceptions.AppointmentNotFoundException;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.persistence.utils.QueryBuilder;
import ar.edu.itba.paw.persistence.utils.UpdateBuilder;
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
public class AppointmentDaoImpl implements AppointmentDao {

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
      Patient patient,
      Doctor doctor,
      LocalDate date,
      ThirtyMinuteBlock timeBlock,
      String description) {

    Map<String, Object> data = new HashMap<>();

    data.put("patient_id", patient.getId());
    data.put("doctor_id", doctor.getId());
    data.put("appointment_date", Date.valueOf(date));
    data.put("appointment_time", timeBlockToSmallInt(timeBlock));
    data.put("appointment_description", description);
    data.put("status_code", AppointmentStatus.CONFIRMED.ordinal());

    long appointmentId = appointmentInsert.executeAndReturnKey(data).longValue();

    return new Appointment(
        appointmentId,
        patient,
        doctor,
        date,
        timeBlock,
        AppointmentStatus.CONFIRMED,
        description,
        null);
  }

  // ========================== Updates ==========================

  @Override
  public Appointment updateAppointment(
      long appointmentId, AppointmentStatus status, String cancelDescription)
      throws AppointmentNotFoundException {
    String update =
        new UpdateBuilder()
            .update("appointment")
            .set("status_code", Integer.toString(status.ordinal()))
            .set("cancel_description", "'" + cancelDescription + "'")
            .where("appointment_id = " + appointmentId)
            .build();

    jdbcTemplate.update(update);
    return getAppointmentById(appointmentId).orElseThrow(AppointmentNotFoundException::new);
  }

  @Override
  public void completeAppointmentsInDate(LocalDate date) {
    String update =
        new UpdateBuilder()
            .update("appointment")
            .set("status_code", Integer.toString(AppointmentStatus.COMPLETED.ordinal()))
            .where("appointment_date = '" + Date.valueOf(date) + "'")
            .where("status_code = " + AppointmentStatus.CONFIRMED.ordinal())
            .build();

    jdbcTemplate.update(update);
  }

  // ========================== Queries ==========================

  @Override
  public Optional<Appointment> getAppointmentById(long appointmentId) {
    String query =
        appointmentsQuery(null, null, null, null, null, null, null)
            .where("appointment_id = '" + appointmentId + "'")
            .build();

    return jdbcTemplate.query(query, RowMappers.APPOINTMENT_EXTRACTOR).stream().findFirst();
  }

  @Override
  public Optional<Appointment> getAppointment(
      long doctorId, LocalDate date, ThirtyMinuteBlock timeBlock) {
    String query =
        appointmentsQuery(doctorId, false, null, null, null, null, null)
            .where("appointment_date = '" + Date.valueOf(date) + "'")
            .where("appointment_time = " + timeBlockToSmallInt(timeBlock))
            .build();

    return jdbcTemplate.query(query, RowMappers.APPOINTMENT_EXTRACTOR).stream().findFirst();
  }

  @Override
  public List<Appointment> getAppointments(long userId, boolean isPatient) {
    String query = appointmentsQuery(userId, isPatient, null, null, null, null, null).build();
    return jdbcTemplate.query(query, RowMappers.APPOINTMENT_EXTRACTOR);
  }

  @Override
  public Page<Appointment> getFilteredAppointments(
      long userId,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      Integer page,
      Integer pageSize,
      boolean isPatient) {

    // Get the appointments for the doctor
    String appointmentsQuery =
        appointmentsQuery(userId, isPatient, status, from, to, page, pageSize).build();

    List<Appointment> appointments =
        jdbcTemplate.query(appointmentsQuery, RowMappers.APPOINTMENT_EXTRACTOR);

    // Get the total number of appointments for the doctor
    String appointmentsCountQuery =
        appointmentsCountQuery(userId, isPatient, status, from, to).build();

    int totalAppointments = jdbcTemplate.queryForObject(appointmentsCountQuery, Integer.class);

    return new Page<>(appointments, page, totalAppointments, pageSize);
  }

  @Override
  public boolean hasPatientMetDoctor(long patientId, long doctorId) {
    String query =
        new QueryBuilder()
            .select("count(*)")
            .from("appointment")
            .where("patient_id = " + patientId)
            .where("doctor_id = " + doctorId)
            .where("status_code = " + AppointmentStatus.COMPLETED.ordinal())
            .build();

    return jdbcTemplate.queryForObject(query, Integer.class) > 0;
  }

  @Override
  public List<Appointment> getAllConfirmedAppointmentsInDate(LocalDate date) {
    String query =
        appointmentsQuery(null, null, AppointmentStatus.CONFIRMED, date, date, null, null).build();

    return jdbcTemplate.query(query, RowMappers.APPOINTMENT_EXTRACTOR);
  }

  // ========================== Private ==========================

  private QueryBuilder appointmentsQuery(
      Long userId,
      Boolean isPatient,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      Integer page,
      Integer pageSize) {

    String ratingQuery =
        new QueryBuilder()
            .select("AVG(rating)")
            .from("review")
            .where("doctor_id = appointment.doctor_id")
            .build();

    String ratingCountQuery =
        new QueryBuilder()
            .select("COUNT(*)")
            .from("review")
            .where("doctor_id = appointment.doctor_id")
            .build();

    QueryBuilder appointmentsQuery =
        new QueryBuilder()
            .select(
                "appointment_id",
                "appointment_date",
                "appointment_time",
                "status_code",
                "appointment_description",
                "cancel_description",
                "appointment.doctor_id",
                "appointment.patient_id",
                "patient_data.email as patient_email",
                "patient_data.password as patient_password",
                "patient_data.first_name as patient_first_name",
                "patient_data.last_name as patient_last_name",
                "health_insurance_for_patient.health_insurance_code as"
                    + " patient_health_insurance_code",
                "patient_data.profile_picture_id as patient_profile_picture_id",
                "doctor_data.email",
                "doctor_data.password",
                "doctor_data.first_name",
                "doctor_data.last_name",
                "doctor_data.profile_picture_id",
                "specialty_code",
                "city_code",
                "location_for_doctor.doctor_location_id",
                "(" + ratingQuery + ") as rating",
                "(" + ratingCountQuery + ") as rating_count",
                "health_insurance_accepted_by_doctor.health_insurance_code",
                "address",
                "monday",
                "tuesday",
                "wednesday",
                "thursday",
                "friday",
                "saturday",
                "sunday")
            .from("appointment")
            .innerJoin("doctor", "appointment.doctor_id = doctor.doctor_id")
            .innerJoin("users as doctor_data", "appointment.doctor_id = doctor_data.user_id")
            .innerJoin(
                "location_for_doctor", "appointment.doctor_id = location_for_doctor.doctor_id")
            .innerJoin(
                "doctor_location",
                "location_for_doctor.doctor_location_id = doctor_location.doctor_location_id")
            .innerJoin(
                "health_insurance_accepted_by_doctor",
                "appointment.doctor_id = health_insurance_accepted_by_doctor.doctor_id")
            .innerJoin(
                "doctor_attending_hours",
                "doctor.attending_hours_id = doctor_attending_hours.attending_hours_id")
            .innerJoin("patient", "appointment.patient_id = patient.patient_id")
            .innerJoin("users as patient_data", "appointment.patient_id = patient_data.user_id")
            .innerJoin(
                "health_insurance_for_patient",
                "appointment.patient_id = health_insurance_for_patient.patient_id");

    if (isPatient != null && userId != null) {
      String userField = isPatient ? "appointment.patient_id" : "appointment.doctor_id";
      appointmentsQuery.where(userField + "=" + userId);
    }

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

    if (page != null && page >= 0 && pageSize != null && pageSize > 0) {
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
