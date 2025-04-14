--liquibase formatted sql

--changeset aspeeencinaf:40
INSERT INTO instructor_department (instructor_id, department_id)
VALUES (1, 1);
--changeset aspeeencinaf:41
INSERT INTO instructor_department (instructor_id, department_id)
VALUES (2, 2);
--changeset aspeeencinaf:42
INSERT INTO instructor_department (instructor_id, department_id)
VALUES (3, 3);