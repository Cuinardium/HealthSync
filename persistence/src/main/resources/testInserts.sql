INSERT INTO profile_picture (profile_picture_id, profile_picture)
VALUES (1, X'10'); -- por alguna razon '\x10' no le gusta a hsqldb pero esto si???

INSERT INTO users (user_id, email, password, first_name, last_name, profile_picture_id)
VALUES (1, 'patient@email.com', 'patient_password', 'patient_first_name', 'patient_last_name', NULL);

INSERT INTO patient (patient_id)
VALUES (1);

INSERT INTO health_insurance_for_patient (patient_id, health_insurance_code)
VALUES (1, 1);