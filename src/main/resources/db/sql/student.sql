--liquibase formatted sql

--changeset aspeeencinaf:46
INSERT INTO student (name, surname, email, created_at, updated_at, freshman, version)
VALUES ('Alice', 'Williams', 'alice.williams@student.globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);
--changeset aspeeencinaf:47
INSERT INTO student (name, surname, email, created_at, updated_at, freshman, version)
VALUES ('Bob', 'Miller', 'bob.miller@student.globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);
--changeset aspeeencinaf:48
INSERT INTO student (name, surname, email, created_at, updated_at, freshman, version)
VALUES ('Charlie', 'Garcia', 'charlie.garcia@student.globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);