CREATE TABLE IF NOT EXISTS profile_picture (
    profile_picture_id SERIAL PRIMARY KEY,
    profile_picture    BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id            SERIAL PRIMARY KEY,
    email              VARCHAR(100) NOT NULL UNIQUE,
    password           VARCHAR(100) NOT NULL,
    first_name         VARCHAR(100) NOT NULL,
    last_name          VARCHAR(100) NOT NULL,
    profile_picture_id INTEGER DEFAULT NULL,
    FOREIGN KEY (profile_picture_id) REFERENCES profile_picture (profile_picture_id)
);

CREATE TABLE IF NOT EXISTS patient_data (
    patient_id INTEGER PRIMARY KEY,
    FOREIGN KEY (patient_id) REFERENCES users (user_id)
);

/*
    health_insurance_code: representa el codigo de la obra social
*/
CREATE TABLE IF NOT EXISTS health_insurance_for_patient (
    patient_id            INTEGER NOT NULL,
    health_insurance_code INTEGER NOT NULL,
    PRIMARY KEY (patient_id, health_insurance_code),
    FOREIGN KEY (patient_id) REFERENCES patient_data (patient_id)
);

/*
    city_code: representa el codigo de la ciudad
*/
CREATE TABLE IF NOT EXISTS doctor_location (
    doctor_location_id SERIAL PRIMARY KEY ,
    address            VARCHAR(100) NOT NULL,
    city_code          INTEGER NOT NULL
);

/*
    monday, tuesday, wednesday, thursday, friday, saturday, sunday:
        Contienen 48 flags que representan si el medico atiende en ese bloque de 30 minutos
*/
CREATE TABLE IF NOT EXISTS doctor_attending_hours (
    attending_hours_id SERIAL PRIMARY KEY,
    monday             BIGINT NOT NULL,
    tuesday            BIGINT NOT NULL,
    wednesday          BIGINT NOT NULL,
    thursday           BIGINT NOT NULL,
    friday             BIGINT NOT NULL,
    saturday           BIGINT NOT NULL,
    sunday             BIGINT NOT NULL
);

/*
 specialty_code: representa el codigo de la especialidad del doctor
*/
CREATE TABLE IF NOT EXISTS doctor (
    doctor_id       INTEGER PRIMARY KEY,
    specialty_code  INTEGER NOT NULL,
    attending_hours_id INTEGER NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES users (user_id),
    FOREIGN KEY (attending_hours_id) REFERENCES doctor_attending_hours (attending_hours_id)
);

CREATE TABLE IF NOT EXISTS location_for_doctor
(
    doctor_location_id INTEGER PRIMARY KEY,
    doctor_id          INTEGER NOT NULL,
    FOREIGN KEY (doctor_location_id) REFERENCES doctor_location (doctor_location_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor (doctor_id)
);

/*
    health_insurance_code: representa el codigo de la obra social
*/
CREATE TABLE IF NOT EXISTS health_insurance_accepted_by_doctor (
    doctor_id             INTEGER NOT NULL,
    health_insurance_code INTEGER NOT NULL,
    PRIMARY KEY (doctor_id, health_insurance_code),
    FOREIGN KEY (doctor_id) REFERENCES doctor (doctor_id)
);

/*
 appointment_date: representa la fecha de la cita
 appointment_time: representa un numero de 0 a 47 que representa un bloque de 30 minutos
 status_code:   0 = pendiente,
                1 = aceptada,
                2 = rechazada,
                3 = cancelada
*/
CREATE TABLE IF NOT EXISTS appointment (
    appointment_id   SERIAL PRIMARY KEY ,
    doctor_id        INTEGER NOT NULL,
    patient_id       INTEGER NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time SMALLINT NOT NULL, -- 0 a 47 -> uso smallint para ahorrar espacio
    appointment_description VARCHAR(1000),
    cancel_description VARCHAR(1000) DEFAULT NULL,
    status_code      INTEGER NOT NULL,
    FOREIGN KEY (doctor_id)     REFERENCES doctor (doctor_id),
    FOREIGN KEY (patient_id)    REFERENCES patient_data (patient_id)
);

-- Alter table appointment add column cancelDescription VARCHAR(1000);

/*
    DROP TABLE IF EXISTS appointment;
    DROP TABLE IF EXISTS health_insurance_accepted_by_doctor;
    DROP TABLE IF EXISTS location_for_doctor;
    DROP TABLE IF EXISTS doctor;
    DROP TABLE IF EXISTS doctor_attending_hours;
    DROP TABLE IF EXISTS doctor_location;
    DROP TABLE IF EXISTS health_insurance_for_patient;
    DROP TABLE IF EXISTS patient;
    DROP TABLE IF EXISTS users CASCADE;
    DROP TABLE IF EXISTS profiprofile_picturele_picture;
 */
/*
SELECT appointment_id,appointment_date,appointment_time,status_code,appointment_description,cancel_description,appointment.doctor_id,appointment.patient_id,patient_data.email as patient_email,patient_data.password as patient_password,patient_data.first_name as patient_first_name, patient_data.last_name as patient_last_name,patient_data.profile_picture_id as patient_profile_picture_id,doctor_data.email,doctor_data.password,doctor_data.first_name,doctor_data.last_name,doctor_data.profile_picture_id,specialty_code,city_code,location_for_doctor.doctor_location_id,address,monday,tuesday,wednesday,thursday,friday,saturday,sunday FROM appointment INNER JOIN doctor ON appointment.doctor_id = doctor.doctor_id INNER JOIN users as doctor_data ON appointment.doctor_id = user_id INNER JOIN location_for_doctor ON appointment.doctor_id = location_for_doctor.doctor_id INNER JOIN doctor_location ON location_for_doctor.doctor_location_id = doctor_location.doctor_location_id INNER JOIN health_insurance_accepted_by_doctor ON appointment.doctor_id = health_insurance_accepted_by_doctor.doctor_id INNER JOIN doctor_attending_hours ON doctor.attending_hours_id = doctor_attending_hours.attending_hours_id INNER JOIN users as patient_data ON appointment.patient_id = patient_data.user_id INNER JOIN users as patient ON appointment.patient_id = patient_data.user_id INNER JOIN health_insurance_for_patient ON appointment.patient_id = health_insurance_for_patient.patient_id WHERE appointment.patient_id=2 ORDER BY appointment_date ASC, appointment_time ASC OFFSET 0 LIMIT 100;
SELECT * FROM appointment INNER JOIN doctor ON appointment.doctor_id = doctor.doctor_id INNER JOIN users as doctor_data ON appointment.doctor_id = user_id INNER JOIN location_for_doctor ON appointment.doctor_id = location_for_doctor.doctor_id INNER JOIN doctor_location ON location_for_doctor.doctor_location_id = doctor_location.doctor_location_id INNER JOIN health_insurance_accepted_by_doctor ON appointment.doctor_id = health_insurance_accepted_by_doctor.doctor_id INNER JOIN doctor_attending_hours ON doctor.attending_hours_id = doctor_attending_hours.attending_hours_id INNER JOIN users as patient_data ON appointment.patient_id = patient_data.user_id INNER JOIN patient ON appointment.patient_id = patient.patient_id INNER JOIN health_insurance_for_patient ON appointment.patient_id = health_insurance_for_patient.patient_id WHERE appointment.patient_id=2 ORDER BY appointment_date ASC, appointment_time ASC OFFSET 0 LIMIT 100

SELECT appointment_id,appointment_date,appointment_time,status_code,appointment_description,cancel_description,appointment.doctor_id,appointment.patient_id,patient_data.email as patient_email,patient_data.password as patient_password,patient_data.first_name as patient_first_name,patient_data.last_name as patient_last_name,patient_data.profile_picture_id as patient_profile_picture_id,doctor_data.email,doctor_data.password,doctor_data.first_name,doctor_data.last_name,doctor_data.profile_picture_id,specialty_code,city_code,location_for_doctor.doctor_location_id,address,monday,tuesday,wednesday,thursday,friday,saturday,sunday FROM appointment INNER JOIN doctor ON appointment.doctor_id = doctor.doctor_id INNER JOIN users as doctor_data ON appointment.doctor_id = doctor_data.user_id INNER JOIN location_for_doctor ON appointment.doctor_id = location_for_doctor.doctor_id INNER JOIN doctor_location ON location_for_doctor.doctor_location_id = doctor_location.doctor_location_id INNER JOIN health_insurance_accepted_by_doctor ON appointment.doctor_id = health_insurance_accepted_by_doctor.doctor_id INNER JOIN doctor_attending_hours ON doctor.attending_hours_id = doctor_attending_hours.attending_hours_id INNER JOIN patient ON appointment.patient_id = patient.patient_id INNER JOIN users as patient_data ON appointment.patient_id = patient_data.user_id INNER JOIN health_insurance_for_patient ON appointment.patient_id = health_insurance_for_patient.patient_id WHERE appointment.patient_id=2 ORDER BY appointment_date ASC, appointment_time ASC OFFSET 0 LIMIT 100;
SELECT appointment_id,appointment_date,appointment_time,status_code,appointment_description,cancel_description,appointment.doctor_id,appointment.patient_id,patient_data.email as patient_email,patient_data.password as patient_password,patient_data.first_name as patient_first_name,patient_data.last_name as patient_last_name,patient_data.profile_picture_id as patient_profile_picture_id,doctor_data.email,doctor_data.password,doctor_data.first_name,doctor_data.last_name,doctor_data.profile_picture_id,specialty_code,city_code,location_for_doctor.doctor_location_id,health_insurance_accepted_by_doctor.health_insurance_code,address,monday,tuesday,wednesday,thursday,friday,saturday,sunday FROM appointment INNER JOIN doctor ON appointment.doctor_id = doctor.doctor_id INNER JOIN users as doctor_data ON appointment.doctor_id = doctor_data.user_id INNER JOIN location_for_doctor ON appointment.doctor_id = location_for_doctor.doctor_id INNER JOIN doctor_location ON location_for_doctor.doctor_location_id = doctor_location.doctor_location_id INNER JOIN health_insurance_accepted_by_doctor ON appointment.doctor_id = health_insurance_accepted_by_doctor.doctor_id INNER JOIN doctor_attending_hours ON doctor.attending_hours_id = doctor_attending_hours.attending_hours_id INNER JOIN patient ON appointment.patient_id = patient.patient_id INNER JOIN users as patient_data ON appointment.patient_id = patient_data.user_id INNER JOIN health_insurance_for_patient ON appointment.patient_id = health_insurance_for_patient.patient_id WHERE appointment.patient_id=2 AND status_code = 0 ORDER BY appointment_date ASC, appointment_time ASC OFFSET 0 LIMIT 100
SELECT appointment_id,appointment_date,appointment_time,status_code,appointment_description,cancel_description,appointment.doctor_id,appointment.patient_id,patient_data.email as patient_email,patient_data.password as patient_password,patient_data.first_name as patient_first_name,patient_data.last_name as patient_last_name,patient_data.profile_picture_id as patient_profile_picture_id,doctor_data.email,doctor_data.password,doctor_data.first_name,doctor_data.last_name,doctor_data.profile_picture_id,specialty_code,city_code,location_for_doctor.doctor_location_id,health_insurance_accepted_by_doctor.health_insurance_code,address,monday,tuesday,wednesday,thursday,friday,saturday,sunday FROM appointment INNER JOIN doctor ON appointment.doctor_id = doctor.doctor_id INNER JOIN users as doctor_data ON appointment.doctor_id = doctor_data.user_id INNER JOIN location_for_doctor ON appointment.doctor_id = location_for_doctor.doctor_id INNER JOIN doctor_location ON location_for_doctor.doctor_location_id = doctor_location.doctor_location_id INNER JOIN health_insurance_accepted_by_doctor ON appointment.doctor_id = health_insurance_accepted_by_doctor.doctor_id INNER JOIN doctor_attending_hours ON doctor.attending_hours_id = doctor_attending_hours.attending_hours_id INNER JOIN patient ON appointment.patient_id = patient.patient_id INNER JOIN users as patient_data ON appointment.patient_id = patient_data.user_id INNER JOIN health_insurance_for_patient ON appointment.patient_id = health_insurance_for_patient.patient_id WHERE appointment.patient_id=2 AND status_code = 0 ORDER BY appointment_date ASC, appointment_time ASC OFFSET 0 LIMIT 100x

*/
