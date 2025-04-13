--liquibase formatted sql

--changeset aspeeencinaf:41
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (1, 1, CURRENT_TIMESTAMP);
--changeset aspeeencinaf:42
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (2, 2, CURRENT_TIMESTAMP);
--changeset aspeeencinaf:43
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (3, 3, CURRENT_TIMESTAMP);
--changeset aspeeencinaf:44
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (1, 2, CURRENT_TIMESTAMP);
--changeset aspeeencinaf:45
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (2, 3, CURRENT_TIMESTAMP);