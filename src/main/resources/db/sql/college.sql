--liquibase formatted sql

--changeset aspeeencinaf:25
INSERT INTO college (name, dean, university_id, version)
VALUES ('College of Engineering', 'Dr. Alan Turing', 1, 1);
--changeset aspeeencinaf:26
INSERT INTO college (name, dean, university_id, version)
VALUES ('College of Arts and Sciences', 'Dr. Marie Curie', 1, 1);