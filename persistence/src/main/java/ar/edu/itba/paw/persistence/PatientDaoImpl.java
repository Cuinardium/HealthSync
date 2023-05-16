package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.persistence.utils.QueryBuilder;
import ar.edu.itba.paw.persistence.utils.UpdateBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PatientDaoImpl implements PatientDao {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert patientInsert;
  private final SimpleJdbcInsert patientHealthInsuranceInsert;

  @Autowired
  public PatientDaoImpl(DataSource ds) {
    this.jdbcTemplate = new JdbcTemplate(ds);
    this.patientInsert = new SimpleJdbcInsert(ds).withTableName("patient");
    this.patientHealthInsuranceInsert =
        new SimpleJdbcInsert(ds).withTableName("health_insurance_for_patient");
  }

  // ======================== Inserts =========================================

  @Override
  public void addHealthInsurance(long patientId, int healthInsuranceCode) {

    Map<String, Object> data = new HashMap<>();

    data.put("patient_id", patientId);
    data.put("health_insurance_code", healthInsuranceCode);

    patientHealthInsuranceInsert.execute(data);
  }

  @Override
  public long createPatient(long userId) {

    Map<String, Object> data = new HashMap<>();

    data.put("patient_id", userId);

    patientInsert.execute(data);

    return userId;
  }

  // ======================== Updates =========================================
  @Override
  public void updatePatientInfo(long patientId, int healthInsuranceCode) {
    String update =
        new UpdateBuilder()
            .update("health_insurance_for_patient")
            .set("health_insurance_code", "'" + healthInsuranceCode + "'")
            .where("patient_id = (" + patientId + ")")
            .build();

    jdbcTemplate.update(update);
  }

  // ============================ Queries =============================================

  @Override
  public Optional<Patient> getPatientById(long id) {

    String query = patientQuery().where("patient.patient_id = " + id).build();

    return jdbcTemplate.query(query, RowMappers.PATIENT_MAPPER).stream().findFirst();
  }

  // ================================= Private ======================================
  private QueryBuilder patientQuery() {
    return new QueryBuilder()
        .select(
            "patient.patient_id",
            "email as patient_email",
            "password as patient_password",
            "first_name as patient_first_name",
            "last_name as patient_last_name",
            "profile_picture_id as patient_profile_picture_id",
            "health_insurance_code as patient_health_insurance_code")
        .from("patient")
        .innerJoin("users", "patient_id = user_id")
        .innerJoin(
            "health_insurance_for_patient",
            "patient.patient_id = health_insurance_for_patient.patient_id");
  }
}
