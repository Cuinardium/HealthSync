INSERT INTO profile_picture (profile_picture_id, profile_picture)
VALUES (2, X'10'); -- por alguna razon '\x10' no le gusta a hsqldb pero esto si???

INSERT INTO users (user_id, email, password, first_name, last_name, profile_picture_id, locale, is_verified)
VALUES (5, 'patient@email.com', 'patient_password', 'patient_first_name', 'patient_last_name', NULL, 'en', 1),
       (8, 'patient2@email.com', 'patient2_password', 'patient2_first_name', 'patient2_last_name', NULL, 'en', 1),
       (9, 'patient3@email.com', 'patient3_password', 'patient3_first_name', 'patient3_last_name', NULL, 'en', 1),
       (10,'patient4@email.com', 'patient4_password', 'patient4_first_name', 'patient4_last_name', NULL, 'en', 1),
       (7, 'doctor@email.com', 'doctor_password', 'doctor_first_name', 'doctor_last_name', NULL, 'en', 1),
       (11, 'doctor2@email.com', 'doctor2_password', 'doctor2_first_name', 'doctor2_last_name', NULL, 'en', 1),
       (12, 'doctor3@email.com', 'doctor3_password', 'doctor3_first_name', 'doctor3_last_name', NULL, 'en', 1);

INSERT INTO patient (patient_id, health_insurance_code)
VALUES (5, 1),
       (8, 1),
       (9, 1),
       (10, 1);

INSERT INTO doctor (doctor_id, specialty_code, city, address)
VALUES (7, 1, 'Adolfo Gonzalez Chaves', 'doctor_address'),
       (11, 2, 'CABA', 'doctor2_address'),
       (12, 3, 'Cordoba', 'doctor3_address');

INSERT INTO doctor_attending_hours (doctor_id, day, hour_block)
VALUES (7,0,0), (7,1,0), (7,2,0), (7,3,0), (7,4,0);

INSERT INTO doctor_vacation (vacation_id, doctor_id, from_date, from_time, to_date, to_time)
VALUES (2, 7, '2020-01-01', 0, '2020-01-10', 0),
       (3, 7, '2020-02-01', 0, '2020-02-10', 0);

INSERT INTO health_insurance_accepted_by_doctor (doctor_id, health_insurance_code)
VALUES (7, 1), (7, 2), (11, 3), (12, 4);

INSERT INTO appointment (appointment_id, patient_id, doctor_id, appointment_date, appointment_time, status_code, appointment_description, cancel_description)
VALUES (3, 5, 7, '2023-05-17', 1, 0, 'Me duele la cabeza', NULL),
       (4, 5, 7, '2023-05-17', 2, 0, 'Hola', NULL),
       (6, 8, 7, '2023-05-17', 1, 0, 'Confirmed', NULL),
       (7, 9, 7, '2023-05-17', 1, 1, 'Cancelled', 'cancelled'),
       (8, 10, 7, '2023-05-17', 1, 2, 'Completed', NULL);

INSERT INTO review (review_id, patient_id, doctor_id, review_date, review_description, rating)
VALUES (7, 5, 7, '2023-05-17', 'Muy buen doctor', 5),
       (8, 5, 7, '2023-05-16', 'Buen doctor', 4),
       (9, 5, 7, '2023-05-15', 'Regular doctor', 3),
       (10, 5, 7, '2023-05-14', 'Malo doctor', 2),
       (11, 5, 7, '2023-05-13', 'Muy malo doctor', 1);

