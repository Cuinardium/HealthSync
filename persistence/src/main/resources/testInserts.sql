INSERT INTO profile_picture (profile_picture_id, profile_picture)
VALUES (1, X'10'); -- por alguna razon '\x10' no le gusta a hsqldb pero esto si???

INSERT INTO users (user_id, email, password, first_name, last_name, profile_picture_id)
VALUES (1, 'patient@email.com', 'patient_password', 'patient_first_name', 'patient_last_name', NULL),
       (2, 'notpatient@email.com', 'notpatient_password', 'notpatient_first_name', 'notpatient_last_name', NULL),
       (3, 'doctor@email.com', 'doctor_password', 'doctor_first_name', 'doctor_last_name', NULL),
       (4, 'notdoctor@email.com', 'notdoctor_password', 'notdoctor_first_name', 'notdoctor_last_name', NULL);

INSERT INTO patient (patient_id)
VALUES (1);

INSERT INTO health_insurance_for_patient (patient_id, health_insurance_code)
VALUES (1, 1);

INSERT INTO doctor_attending_hours (attending_hours_id, monday, tuesday, wednesday, thursday, friday, saturday, sunday)
VALUES (1, 1, 1, 1, 1, 1, 0, 0);

INSERT INTO doctor (doctor_id, specialty_code, attending_hours_id)
VALUES (3, 1, 1);

INSERT INTO doctor_location(doctor_location_id, address, city_code)
VALUES (1, 'doctor_address', 1);

INSERT INTO location_for_doctor (doctor_id, doctor_location_id)
VALUES (3, 1);

INSERT INTO health_insurance_accepted_by_doctor (doctor_id, health_insurance_code)
VALUES (3, 1), (3, 2);

INSERT INTO appointment (appointment_id, patient_id, doctor_id, appointment_date, appointment_time, status_code, appointment_description, cancel_description)
VALUES (1, 1, 3, '2023-05-17', 1, 0, 'Me duele la cabeza', NULL),
       (2, 1, 3, '2023-05-17', 2, 0, 'Hola', NULL);

INSERT INTO review (review_id, patient_id, doctor_id, review_date, review_description, rating)
VALUES (1, 1, 3, '2023-05-17', 'Muy buen doctor', 5),
       (2, 1, 3, '2023-05-16', 'Buen doctor', 4),
       (3, 1, 3, '2023-05-15', 'Regular doctor', 3),
       (4, 1, 3, '2023-05-14', 'Malo doctor', 2),
       (5, 1, 3, '2023-05-13', 'Muy malo doctor', 1);