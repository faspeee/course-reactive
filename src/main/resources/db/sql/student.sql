--liquibase formatted sql

--changeset aspeeencinaf:47
INSERT INTO student (id, name, surname, email, created_at, updated_at, freshman, version)
VALUES ('1eb7de5d-746c-493b-865e-b524d7e65d49', 'Alice', 'Williams', 'alice.williams@student.globaltech.edu',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);
--changeset aspeeencinaf:48
INSERT INTO student (id, name, surname, email, created_at, updated_at, freshman, version)
VALUES ('23aee4d8-41a5-44ed-9fe7-167aea20066e', 'Bob', 'Miller', 'bob.miller@student.globaltech.edu', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 'Yes', 1);
--changeset aspeeencinaf:49
INSERT INTO student (id, name, surname, email, created_at, updated_at, freshman, version)
VALUES ('2853e3c3-df5f-4e35-86ff-88b749183b7a', 'Charlie', 'Garcia', 'charlie.garcia@student.globaltech.edu',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);


--This student are for enrollment query
--changeset aspeeencinaf:50
INSERT INTO student (id, name, surname, email, created_at, updated_at, freshman, version)
VALUES ('2853e3c3-df5f-4e35-86ff-78b745183b7a', 'Charlie', 'Garcia', 'charlie.garcia@student.globaltech.edu',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);

--changeset aspeeencinaf:51
INSERT INTO student (id, name, surname, email, created_at, updated_at, freshman, version)
VALUES ('2853e3c3-df5f-4e35-86ff-98b744183b7a', 'Charlie', 'Garcia', 'charlie.garcia@student.globaltech.edu',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);

--changeset aspeeencinaf:52
INSERT INTO student (id, name, surname, email, created_at, updated_at, freshman, version)
VALUES ('2853e3c3-df5f-4e35-86ff-18b743183b7a', 'Charlie', 'Garcia', 'charlie.garcia@student.globaltech.edu',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);

--changeset aspeeencinaf:53
INSERT INTO student (id, name, surname, email, created_at, updated_at, freshman, version)
VALUES ('2853e3c3-df5f-4e35-86ff-28b742183b7a', 'Charlie', 'Garcia', 'charlie.garcia@student.globaltech.edu',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);

--changeset aspeeencinaf:54
INSERT INTO student (id, name, surname, email, created_at, updated_at, freshman, version)
VALUES ('2853e3c3-df5f-4e35-86ff-38b741183b7a', 'Charlie', 'Garcia', 'charlie.garcia@student.globaltech.edu',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);
--This student are for enrollment query