INSERT INTO profile_picture (profile_picture_id, profile_picture)
VALUES (2, X'10'); -- por alguna razon '\x10' no le gusta a hsqldb pero esto si???

INSERT INTO users (user_id, email, password, first_name, last_name, profile_picture_id)
VALUES (5, 'patient@email.com', 'patient_password', 'patient_first_name', 'patient_last_name', NULL),
       (6, 'notpatient@email.com', 'notpatient_password', 'notpatient_first_name', 'notpatient_last_name', NULL),
       (7, 'doctor@email.com', 'doctor_password', 'doctor_first_name', 'doctor_last_name', NULL),
       (8, 'notdoctor@email.com', 'notdoctor_password', 'notdoctor_first_name', 'notdoctor_last_name', NULL);

INSERT INTO patient (patient_id, health_insurance_code)
VALUES (5, 1);

INSERT INTO doctor (doctor_id, specialty_code, city_code, address)
VALUES (7, 1, 1, 'doctor_address');

INSERT INTO doctor_attending_hours (doctor_id, day, hour_block)
VALUES (7,0,0), (7,1,0), (7,2,0), (7,3,0), (7,4,0);

INSERT INTO health_insurance_accepted_by_doctor (doctor_id, health_insurance_code)
VALUES (7, 1), (7, 2);

INSERT INTO appointment (appointment_id, patient_id, doctor_id, appointment_date, appointment_time, status_code, appointment_description, cancel_description)
VALUES (3, 5, 7, '2023-05-17', 1, 0, 'Me duele la cabeza', NULL),
       (4, 5, 7, '2023-05-17', 2, 0, 'Hola', NULL);

INSERT INTO review (review_id, patient_id, doctor_id, review_date, review_description, rating)
VALUES (7, 5, 7, '2023-05-17', 'Muy buen doctor', 5),
       (8, 5, 7, '2023-05-16', 'Buen doctor', 4),
       (9, 5, 7, '2023-05-15', 'Regular doctor', 3),
       (10, 5, 7, '2023-05-14', 'Malo doctor', 2),
       (11, 5, 7, '2023-05-13', 'Muy malo doctor', 1);

