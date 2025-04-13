--liquibase formatted sql

--changeset aspeeencinaf:33
INSERT INTO instructor_department (instructor_id, department_id)
VALUES (1, 1);
--changeset aspeeencinaf:34
INSERT INTO instructor_department (instructor_id, department_id)
VALUES (2, 2);
--changeset aspeeencinaf:35
INSERT INTO instructor_department (instructor_id, department_id)
VALUES (3, 3);