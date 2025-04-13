--liquibase formatted sql

--changeset aspeeencinaf:23
INSERT INTO building (name, code, campus_id, version)
VALUES ('Engineering Hall', 'ENGH', 1, 1);
--changeset aspeeencinaf:24
INSERT INTO building (name, code, campus_id, version)
VALUES ('Science Building', 'SCIB', 1, 1);
--changeset aspeeencinaf:25
INSERT INTO building (name, code, campus_id, version)
VALUES ('Downtown Center', 'DTC', 2, 1);