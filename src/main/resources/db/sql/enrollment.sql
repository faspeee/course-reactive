--liquibase formatted sql

--changeset aspeeencinaf:49
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (1, 1, CURRENT_TIMESTAMP);
--changeset aspeeencinaf:50
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (2, 2, CURRENT_TIMESTAMP);
--changeset aspeeencinaf:51
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (3, 3, CURRENT_TIMESTAMP);
--changeset aspeeencinaf:52
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (1, 2, CURRENT_TIMESTAMP);
--changeset aspeeencinaf:53
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (2, 3, CURRENT_TIMESTAMP);