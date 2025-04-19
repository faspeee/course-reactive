--liquibase formatted sql

--changeset aspeeencinaf:38
INSERT INTO building (id, name, code, campus_id, version)
VALUES ('199d0cb2-be34-48da-8915-9964daf6d724', 'Engineering Hall', 'ENGH', 'e4b44ebc-7369-4c79-96d4-4e0c61034efc', 1);
--changeset aspeeencinaf:39
INSERT INTO building (id, name, code, campus_id, version)
VALUES ('3ceaff23-8f6e-4557-9fc6-c294f64063d4', 'Science Building', 'SCIB', 'e4b44ebc-7369-4c79-96d4-4e0c61034efc', 1);
--changeset aspeeencinaf:40
INSERT INTO building (id, name, code, campus_id, version)
VALUES ('4806db08-b4fd-49d9-b099-2b8b79d13259', 'Downtown Center', 'DTC', '5919b48f-1220-46df-b966-52143ef2f995', 1);