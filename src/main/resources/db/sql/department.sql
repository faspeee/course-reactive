--liquibase formatted sql

--changeset aspeeencinaf:27
INSERT INTO department (name, description, identifier, college_id, version)
VALUES ('Computer Science', 'Focuses on the study of computer systems and computational processes.', 'DSUEOW823', 1, 1);

--changeset aspeeencinaf:28
INSERT INTO department (name, description, identifier, college_id, version)
VALUES ('Electrical Engineering',
        'Deals with the study and application of electricity, electronics, and electromagnetism.', 'SWPSAS83', 1, 1);
--changeset aspeeencinaf:29
INSERT INTO department (name, description, identifier, college_id, version)
VALUES ('Biology', 'Explores the science of life and living organisms.', 'SWPSAS81', 2, 1);