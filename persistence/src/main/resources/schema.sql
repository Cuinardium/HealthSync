-- =================== SET USER TABLES ===================

CREATE TABLE IF NOT EXISTS health_insurance (
    health_insurance_id SERIAL PRIMARY KEY,
    health_insurance_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS profile_picture (
    profile_picture_id SERIAL PRIMARY KEY,
    profile_picture BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    profile_picture_id INTEGER NOT NULL DEFAULT 1,
    is_doctor BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (profile_picture_id) REFERENCES profile_picture (profile_picture_id)
);

CREATE TABLE IF NOT EXISTS health_insurance_for_user(
    health_insurance_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY (health_insurance_id, user_id),
    FOREIGN KEY (health_insurance_id) REFERENCES health_insurance (health_insurance_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- =================== SET DOCTOR TABLES ===================

CREATE TABLE IF NOT EXISTS medical_specialty (
    medical_specialty_id SERIAL PRIMARY KEY,
    medical_specialty_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS medic_location (
    medic_location_id SERIAL PRIMARY KEY ,
    medic_location_city VARCHAR(100) NOT NULL,
    medic_location_address VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS medic (
    medic_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    medical_specialty_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (medical_specialty_id) REFERENCES medical_specialty (medical_specialty_id)
);

CREATE TABLE IF NOT EXISTS medic_location_for_medic(
    medic_location_id INTEGER NOT NULL PRIMARY KEY,
    medic_id INTEGER NOT NULL,
    FOREIGN KEY (medic_location_id) REFERENCES medic_location (medic_location_id),
    FOREIGN KEY (medic_id) REFERENCES medic (medic_id)
);

CREATE TABLE IF NOT EXISTS health_insurance_accepted_by_medic(
    health_insurance_id INTEGER NOT NULL,
    medic_id INTEGER NOT NULL,
    PRIMARY KEY (health_insurance_id, medic_id),
    FOREIGN KEY (health_insurance_id) REFERENCES health_insurance (health_insurance_id),
    FOREIGN KEY (medic_id) REFERENCES medic (medic_id)
);

CREATE TABLE IF NOT EXISTS appointment (
    appointment_id SERIAL PRIMARY KEY,
    medic_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    FOREIGN KEY (medic_id) REFERENCES medic (medic_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

-- IN CASE OF EMERGENCY
/*
DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS health_insurance_accepted_by_medic;
DROP TABLE IF EXISTS medic_location_for_medic;
DROP TABLE IF EXISTS medic;
DROP TABLE IF EXISTS medic_location;
DROP TABLE IF EXISTS medical_specialty;
DROP TABLE IF EXISTS health_insurance_for_user;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS profile_picture;
DROP TABLE IF EXISTS health_insurance;
*/
