--liquibase formatted sql

--changeset aspeeencinaf:43
INSERT INTO course (id, course_name, course_code, start_date, end_date, credit_hours, description, instructor_id,
                    is_active, department_id)
VALUES ('cb3dc0a6-3bab-462d-ae69-2ad134495def', 'Introduction to Programming', 'CS101', '2025-09-01', '2025-12-15', 3,
        'Basic concepts of programming using Python.', '520ee975-1224-42e6-8255-a9712e2bc22c', TRUE,
        '88af4487-6120-4a7f-81fe-a9ebc1072413');
--changeset aspeeencinaf:44
INSERT INTO course (id, course_name, course_code, start_date, end_date, credit_hours, description, instructor_id,
                    is_active, department_id)
VALUES ('7e73ad23-266d-4746-b948-1f78e722cf02', 'Circuit Analysis', 'EE201', '2025-09-01', '2025-12-15', 4,
        'Study of electrical circuits and their components.', '849fdd9f-2535-42f4-9c03-deccde3a0ba4', TRUE,
        '2ecc43cc-8ee8-41ab-b4de-26858b533a0f');
--changeset aspeeencinaf:45
INSERT INTO course (id, course_name, course_code, start_date, end_date, credit_hours, description, instructor_id,
                    is_active, department_id)
VALUES ('7475417a-f970-4366-aa9f-54c7bb66fe0a', 'General Biology', 'BIO101', '2025-09-01', '2025-12-15', 3,
        'Introduction to biological principles and concepts.', '705ac9e9-f37a-4d13-9a93-e933346c2e7f', TRUE,
        '1a6c2f08-8ee8-438f-b37f-01a1c862efe7');

