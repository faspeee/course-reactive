--liquibase formatted sql

--changeset aspeeencinaf:30
INSERT INTO building (name, code, campus_id, version)
VALUES ('Engineering Hall', 'ENGH', 1, 1);
--changeset aspeeencinaf:31
INSERT INTO building (name, code, campus_id, version)
VALUES ('Science Building', 'SCIB', 1, 1);
--changeset aspeeencinaf:32
INSERT INTO building (name, code, campus_id, version)
VALUES ('Downtown Center', 'DTC', 2, 1);