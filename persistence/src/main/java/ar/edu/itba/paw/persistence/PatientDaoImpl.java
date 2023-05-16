package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.PatientDao;
import ar.edu.itba.paw.models.HealthInsurance;
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
  public Patient createPatient(long userId, HealthInsurance healthInsurance) {

    // Insert data in patient table
    Map<String, Object> data = new HashMap<>();

    data.put("patient_id", userId);

    patientInsert.execute(data);

    addHealthInsurance(userId, healthInsurance.ordinal());

    return getPatientById(userId).orElseThrow(IllegalStateException::new);
  }

  // ======================== Updates =========================================
  
  @Override
  public Patient updatePatientInfo(long patientId, HealthInsurance healthInsurance) {
    String update =
        new UpdateBuilder()
            .update("health_insurance_for_patient")
            .set("health_insurance_code", "'" + healthInsurance.ordinal() + "'")
            .where("patient_id = (" + patientId + ")")
            .build();

    jdbcTemplate.update(update);

    return getPatientById(patientId).orElseThrow(IllegalStateException::new);
  }

  // ============================ Queries =============================================

  @Override
  public Optional<Patient> getPatientById(long id) {

    String query = patientQuery().where("patient.patient_id = " + id).build();

    return jdbcTemplate.query(query, RowMappers.PATIENT_MAPPER).stream().findFirst();
  }

  // ================================= Private ======================================

  private void addHealthInsurance(long patientId, int healthInsuranceCode) {

    Map<String, Object> data = new HashMap<>();

    data.put("patient_id", patientId);
    data.put("health_insurance_code", healthInsuranceCode);

    patientHealthInsuranceInsert.execute(data);
  }

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
