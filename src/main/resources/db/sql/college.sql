--liquibase formatted sql

--changeset aspeeencinaf:25
INSERT INTO college (id, name, dean, university_id, version)
VALUES ('19902a0f-e444-4118-9fe0-d5d61e3750dc', 'College of Engineering', 'Dr. Alan Turing',
        '068ec73a-5210-4891-b4d2-a988541e3854', 1);
--changeset aspeeencinaf:26
INSERT INTO college (id, name, dean, university_id, version)
VALUES ('bf4eecee-cfe9-41fc-b56c-9fefee9da858', 'College of Arts and Sciences', 'Dr. Marie Curie',
        '068ec73a-5210-4891-b4d2-a988541e3854', 1);