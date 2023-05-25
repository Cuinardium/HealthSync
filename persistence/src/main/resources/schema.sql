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

CREATE TABLE IF NOT EXISTS patient (
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
    FOREIGN KEY (patient_id) REFERENCES patient (patient_id)
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
 specialty_code: representa el codigo de la especialidad del doctor
*/
CREATE TABLE IF NOT EXISTS doctor (
    doctor_id       INTEGER PRIMARY KEY,
    specialty_code  INTEGER NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS doctor_attending_hours (
    doctor_id INTEGER NOT NULL,
    day SMALLINT NOT NULL,
    hour_block SMALLINT NOT NULL,
    PRIMARY KEY (doctor_id, day, hour_block),
    FOREIGN KEY (doctor_id) REFERENCES doctor (doctor_id)
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
    FOREIGN KEY (patient_id)    REFERENCES patient (patient_id)
);

CREATE TABLE IF NOT EXISTS review (
    review_id        SERIAL PRIMARY KEY,
    doctor_id        INTEGER NOT NULL,
    patient_id       INTEGER NOT NULL,
    review_date      DATE NOT NULL,
    review_description VARCHAR(1000),
    rating           SMALLINT NOT NULL,
    FOREIGN KEY (doctor_id)     REFERENCES doctor (doctor_id),
    FOREIGN KEY (patient_id)    REFERENCES patient (patient_id)
);

/*
    DROP TABLE IF EXISTS appointment;
    DROP TABLE IF EXISTS health_insurance_accepted_by_doctor;
    DROP TABLE IF EXISTS location_for_doctor;
    DROP TABLE IF EXISTS doctor_attending_hours;
    DROP TABLE IF EXISTS review;
    DROP TABLE IF EXISTS doctor;
    DROP TABLE IF EXISTS doctor_location;
    DROP TABLE IF EXISTS health_insurance_for_patient;
    DROP TABLE IF EXISTS patient;
    DROP TABLE IF EXISTS users;
    DROP TABLE IF EXISTS profile_picture;
 */
