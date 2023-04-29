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
    profile_picture_id INTEGER DEFAULT 1 NOT NULL,
    FOREIGN KEY (profile_picture_id) REFERENCES profile_picture (profile_picture_id)
);

CREATE TABLE IF NOT EXISTS patient (
    patient_id INTEGER PRIMARY KEY,
    FOREIGN KEY (patient_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS health_insurance_for_patient (
    patient_id            INTEGER NOT NULL,
    health_insurance_code INTEGER NOT NULL,
    PRIMARY KEY (patient_id, health_insurance_code),
    FOREIGN KEY (patient_id) REFERENCES patient (patient_id)
);

CREATE TABLE IF NOT EXISTS doctor_location (
    doctor_location_id SERIAL PRIMARY KEY ,
    address            VARCHAR(100) NOT NULL,
    city_code          INTEGER NOT NULL
);

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

CREATE TABLE IF NOT EXISTS doctor (
    doctor_id       INTEGER PRIMARY KEY,
    specialty_code  INTEGER NOT NULL,
    attending_hours_id INTEGER NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES users (user_id),
    FOREIGN KEY (attending_hours_id) REFERENCES attending_hours (attending_hours_id)
);

CREATE TABLE IF NOT EXISTS location_for_doctor
(
    doctor_location_id INTEGER PRIMARY KEY,
    doctor_id          INTEGER NOT NULL,
    FOREIGN KEY (doctor_location_id) REFERENCES doctor_location (doctor_location_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor (doctor_id)
);

CREATE TABLE IF NOT EXISTS health_insurance_accepted_by_doctor (
    doctor_id             INTEGER NOT NULL,
    health_insurance_code INTEGER NOT NULL,
    PRIMARY KEY (doctor_id, health_insurance_code),
    FOREIGN KEY (doctor_id) REFERENCES doctor (doctor_id)
);

CREATE TABLE IF NOT EXISTS appointment (
    appointment_id   SERIAL PRIMARY KEY ,
    doctor_id        INTEGER NOT NULL,
    patient_id       INTEGER NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status_code      INTEGER NOT NULL,
    FOREIGN KEY (doctor_id)     REFERENCES doctor (doctor_id),
    FOREIGN KEY (patient_id)    REFERENCES patient (patient_id)
);

/*
DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS health_insurance_accepted_by_doctor;
DROP TABLE IF EXISTS location_for_doctor;
DROP TABLE IF EXISTS doctor;
DROP TABLE IF EXISTS doctor_location;
DROP TABLE IF EXISTS health_insurance_for_patient;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS profile_picture;
 */
