--liquibase formatted sql

--changeset aspeeencinaf:30
INSERT INTO instructor (name, email, created_at, updated_at, version)
VALUES ('Dr. Emily Johnson', 'e.johnson@globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
--changeset aspeeencinaf:31
INSERT INTO instructor (name, email, created_at, updated_at, version)
VALUES ('Dr. Michael Brown', 'm.brown@globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
--changeset aspeeencinaf:32
INSERT INTO instructor (name, email, created_at, updated_at, version)
VALUES ('Dr. Sarah Davis', 's.davis@globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);