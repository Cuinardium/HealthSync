package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.AttendingHours;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.HealthInsurance;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Location;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.ThirtyMinuteBlock;
import ar.edu.itba.paw.models.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class RowMappers {

  protected static final ResultSetExtractor<List<Doctor>> DOCTOR_EXTRACTOR =
      (rs) -> {
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
                    ThirtyMinuteBlock.fromBits(rs.getLong("monday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("tuesday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("wednesday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("thursday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("friday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("saturday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("sunday")));

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
      };

  protected static final RowMapper<Image> IMAGE_MAPPER =
      (rs, rowNum) -> new Image(rs.getLong("profile_picture_id"), rs.getBytes("profile_picture"));

  protected static final RowMapper<Location> LOCATION_MAPPER =
      (rs, rowNum) -> {
        City city = City.values()[rs.getInt("city_code")];

        return new Location(rs.getLong("doctor_location_id"), city, rs.getString("address"));
      };

  protected static final RowMapper<Patient> PATIENT_MAPPER =
      (rs, rowNum) -> {
        HealthInsurance healthInsurance =
            HealthInsurance.values()[rs.getInt("patient_health_insurance_code")];

        return new Patient(
            rs.getLong("patient_id"),
            rs.getString("patient_email"),
            rs.getString("patient_password"),
            rs.getString("patient_first_name"),
            rs.getString("patient_last_name"),
            rs.getObject("patient_profile_picture_id") == null
                ? null
                : rs.getLong("patient_profile_picture_id"),
            healthInsurance);
      };

  protected static final RowMapper<User> USER_MAPPER =
      (rs, rowNum) ->
          new User(
              rs.getLong("user_id"),
              rs.getString("email"),
              rs.getString("password"),
              rs.getString("first_name"),
              rs.getString("last_name"),
              rs.getObject("profile_picture_id") == null ? null : rs.getLong("profile_picture_id"));

  protected static final ResultSetExtractor<List<Appointment>> APPOINTMENT_EXTRACTOR =
      (rs) -> {
        Map<Long, Appointment> appointments = new HashMap<>();

        Specialty[] specialties = Specialty.values();
        HealthInsurance[] healthInsurances = HealthInsurance.values();
        City[] cities = City.values();

        int rowNum = 0;

        while (rs.next()) {
          long appointmentId = rs.getLong("appointment_id");

          if (appointments.get(appointmentId) == null) {
            Patient patient = RowMappers.PATIENT_MAPPER.mapRow(rs, rowNum);

            LocalDate date = rs.getDate("appointment_date").toLocalDate();
            ThirtyMinuteBlock timeBlock =
                ThirtyMinuteBlock.values()[rs.getShort("appointment_time")];
            AppointmentStatus status = AppointmentStatus.values()[rs.getInt("status_code")];
            String description = rs.getString("appointment_description");
            String cancelDesc = rs.getString("cancel_description");

            long doctorId = rs.getLong("doctor_id");
            String doctorEmail = rs.getString("email");
            String doctorPassword = rs.getString("password");
            String doctorFirstName = rs.getString("first_name");
            String doctorLastName = rs.getString("last_name");
            Long doctorPfpId =
                rs.getObject("profile_picture_id") == null
                    ? null
                    : rs.getLong("profile_picture_id");

            Specialty specialty = specialties[rs.getInt("specialty_code")];

            City city = cities[rs.getInt("city_code")];
            String address = rs.getString("address");
            long locationId = rs.getLong("doctor_location_id");
            Location location = new Location(locationId, city, address);

            AttendingHours attendingHours =
                new AttendingHours(
                    ThirtyMinuteBlock.fromBits(rs.getLong("monday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("tuesday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("wednesday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("thursday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("friday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("saturday")),
                    ThirtyMinuteBlock.fromBits(rs.getLong("sunday")));

            Doctor doctor =
                new Doctor(
                    doctorId,
                    doctorEmail,
                    doctorPassword,
                    doctorFirstName,
                    doctorLastName,
                    doctorPfpId,
                    new ArrayList<>(),
                    specialty,
                    location,
                    attendingHours);

            Appointment appointment =
                new Appointment(
                    appointmentId,
                    patient,
                    doctor,
                    date,
                    timeBlock,
                    status,
                    description,
                    cancelDesc);
            appointments.put(appointmentId, appointment);
          }

          appointments
              .get(appointmentId)
              .getDoctor()
              .getHealthInsurances()
              .add(healthInsurances[rs.getInt("health_insurance_code")]);

          rowNum++;
        }

        return new ArrayList<>(appointments.values());
      };
}
