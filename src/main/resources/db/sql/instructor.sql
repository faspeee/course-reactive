--liquibase formatted sql

--changeset aspeeencinaf:37
INSERT INTO instructor (id, name, email, created_at, updated_at, version)
VALUES ('520ee975-1224-42e6-8255-a9712e2bc22c', 'Dr. Emily Johnson', 'e.johnson@globaltech.edu', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 1);
--changeset aspeeencinaf:38
INSERT INTO instructor (id, name, email, created_at, updated_at, version)
VALUES ('849fdd9f-2535-42f4-9c03-deccde3a0ba4', 'Dr. Michael Brown', 'm.brown@globaltech.edu', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 1);
--changeset aspeeencinaf:39
INSERT INTO instructor (id, name, email, created_at, updated_at, version)
VALUES ('705ac9e9-f37a-4d13-9a93-e933346c2e7f', 'Dr. Sarah Davis', 's.davis@globaltech.edu', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 1);