package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.AppointmentDao;
import ar.edu.itba.paw.interfaces.persistence.DoctorDao;
import ar.edu.itba.paw.interfaces.persistence.PatientDao;
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

  private final PatientDao patientDao;
  private final DoctorDao doctorDao;

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert appointmentInsert;

  @Autowired
  public AppointmentDaoImpl(DataSource ds, PatientDao patientDao, DoctorDao doctorDao) {
    this.jdbcTemplate = new JdbcTemplate(ds);
    this.appointmentInsert =
        new SimpleJdbcInsert(ds)
            .withTableName("appointment")
            .usingGeneratedKeyColumns("appointment_id");
    this.patientDao = patientDao;
    this.doctorDao = doctorDao;
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
    data.put("status_code", AppointmentStatus.PENDING.ordinal());

    long appointmentId = appointmentInsert.executeAndReturnKey(data).longValue();

    return new Appointment(
        appointmentId,
        patient,
        doctor,
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
        appointmentsQuery(-1, null, null, null, null, -1, -1)
            .where("appointment_id = '" + appointmentId + "'")
            .build();

    return jdbcTemplate.query(query, RowMappers.APPOINTMENT_MAPPER).stream().findFirst();
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

    return jdbcTemplate.query(query, RowMappers.APPOINTMENT_MAPPER).stream().findFirst();
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

    return jdbcTemplate.query(query, RowMappers.APPOINTMENT_MAPPER);
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

    return jdbcTemplate.query(query, RowMappers.APPOINTMENT_MAPPER);
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

    List<Appointment> appointments =
        jdbcTemplate.query(appointmentsQuery, RowMappers.APPOINTMENT_MAPPER);

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

    List<Appointment> appointments =
        jdbcTemplate.query(appointmentsQuery, RowMappers.APPOINTMENT_MAPPER);

    // Get the total number of appointments for the patient
    String appointmentsCountQuery =
        appointmentsCountQuery(patientId, false, status, from, to).build();

    int totalAppointments = jdbcTemplate.queryForObject(appointmentsCountQuery, Integer.class);

    return new Page<>(appointments, page, totalAppointments);
  }

  // ========================== Private ==========================

  private QueryBuilder appointmentsQuery(
      long userId,
      Boolean isDoctor,
      AppointmentStatus status,
      LocalDate from,
      LocalDate to,
      int page,
      int pageSize) {

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
                "health_insurance_for_patient.health_insurance_code as patient_health_insurance_code",
                "patient_data.profile_picture_id as patient_profile_picture_id",
                "doctor_data.email",
                "doctor_data.password",
                "doctor_data.first_name",
                "doctor_data.last_name",
                "doctor_data.profile_picture_id",
                "specialty_code",
                "city_code",
                "location_for_doctor.doctor_location_id",
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

    if (isDoctor != null) {
      String userField = isDoctor ? "appointment.doctor_id" : "appointment.patient_id";
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
