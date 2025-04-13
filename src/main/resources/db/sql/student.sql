--liquibase formatted sql

--changeset aspeeencinaf:39
INSERT INTO student (name, surname, email, created_at, updated_at, freshman, version)
VALUES ('Alice', 'Williams', 'alice.williams@student.globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);
--changeset aspeeencinaf:40
INSERT INTO student (name, surname, email, created_at, updated_at, freshman, version)
VALUES ('Bob', 'Miller', 'bob.miller@student.globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);
--changeset aspeeencinaf:41
INSERT INTO student (name, surname, email, created_at, updated_at, freshman, version)
VALUES ('Charlie', 'Garcia', 'charlie.garcia@student.globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);