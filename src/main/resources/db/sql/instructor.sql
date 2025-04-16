--liquibase formatted sql

--changeset aspeeencinaf:37
INSERT INTO instructor (name, email, created_at, updated_at, version)
VALUES ('Dr. Emily Johnson', 'e.johnson@globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
--changeset aspeeencinaf:38
INSERT INTO instructor (name, email, created_at, updated_at, version)
VALUES ('Dr. Michael Brown', 'm.brown@globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
--changeset aspeeencinaf:39
INSERT INTO instructor (name, email, created_at, updated_at, version)
VALUES ('Dr. Sarah Davis', 's.davis@globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);