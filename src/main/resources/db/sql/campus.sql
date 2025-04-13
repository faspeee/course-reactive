--liquibase formatted sql

--changeset aspeeencinaf:16
INSERT INTO campus (name, address, country, city, university_id, version)
VALUES ('Main Campus', '123 UniversityService Ave, Tech City', 'Usa', 'George', 1, 1);

--changeset aspeeencinaf:17
INSERT INTO campus (name, address, country, city, university_id, version)
VALUES ('Downtown Campus', '456 City Center Blvd, Tech City', 'Italia', 'Bologna', 1, 1);