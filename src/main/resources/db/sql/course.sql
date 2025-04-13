--liquibase formatted sql

--changeset aspeeencinaf:36
INSERT INTO course (course_name, course_code, start_date, end_date, credit_hours, description, instructor_id, is_active,
                    department_id)
VALUES ('Introduction to Programming', 'CS101', '2025-09-01', '2025-12-15', 3,
        'Basic concepts of programming using Python.', 1, TRUE, 1);
--changeset aspeeencinaf:37
INSERT INTO course (course_name, course_code, start_date, end_date, credit_hours, description, instructor_id, is_active,
                    department_id)
VALUES ('Circuit Analysis', 'EE201', '2025-09-01', '2025-12-15', 4,
        'Study of electrical circuits and their components.', 2, TRUE, 2);
--changeset aspeeencinaf:38
INSERT INTO course (course_name, course_code, start_date, end_date, credit_hours, description, instructor_id, is_active,
                    department_id)
VALUES ('General Biology', 'BIO101', '2025-09-01', '2025-12-15', 3,
        'Introduction to biological principles and concepts.', 3, TRUE, 3);

