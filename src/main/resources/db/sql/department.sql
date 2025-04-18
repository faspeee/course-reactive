--liquibase formatted sql

--changeset aspeeencinaf:32
INSERT INTO department (id, name, description, identifier, college_id, version)
VALUES ('88af4487-6120-4a7f-81fe-a9ebc1072413', 'Computer Science',
        'Focuses on the study of computer systems and computational processes.', 'DSUEOW823',
        '19902a0f-e444-4118-9fe0-d5d61e3750dc', 1);

--changeset aspeeencinaf:33
INSERT INTO department (id, name, description, identifier, college_id, version)
VALUES ('2ecc43cc-8ee8-41ab-b4de-26858b533a0f', 'Electrical Engineering',
        'Deals with the study and application of electricity, electronics, and electromagnetism.', 'SWPSAS83',
        '19902a0f-e444-4118-9fe0-d5d61e3750dc', 1);
--changeset aspeeencinaf:34
INSERT INTO department (id, name, description, identifier, college_id, version)
VALUES ('1a6c2f08-8ee8-438f-b37f-01a1c862efe7', 'Biology', 'Explores the science of life and living organisms.',
        'SWPSAS81', 'bf4eecee-cfe9-41fc-b56c-9fefee9da858', 1);


--changeset aspeeencinaf:35
INSERT INTO department (id, name, description, identifier, college_id, version)
VALUES ('88a14487-6120-4a7f-81fe-a9ebc1072413', 'Computer Science',
        'Focuses on the study of computer systems and computational processes.', 'DSUEOW823',
        '1990230f-e444-4118-9fe0-d5d61e3750dc', 1);

--changeset aspeeencinaf:36
INSERT INTO department (id, name, description, identifier, college_id, version)
VALUES ('2ecc44cc-8ee8-41ab-b4de-26858b533a0f', 'Electrical Engineering',
        'Deals with the study and application of electricity, electronics, and electromagnetism.', 'SWPSAS83',
        '1990230f-e444-4118-9fe0-d5d61e3750dc', 1);
--changeset aspeeencinaf:37
INSERT INTO department (id, name, description, identifier, college_id, version)
VALUES ('1a6c2f08-81e8-438f-b37f-01a1c862efe7', 'Biology', 'Explores the science of life and living organisms.',
        'SWPSAS81', '068ec13a-5210-4891-b4d2-a988541e3854', 1);