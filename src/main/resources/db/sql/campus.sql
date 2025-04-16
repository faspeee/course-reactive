--liquibase formatted sql

--changeset aspeeencinaf:23
INSERT INTO campus (id, name, address, country, city, university_id, version)
VALUES ('e4b44ebc-7369-4c79-96d4-4e0c61034efc', 'Main Campus', '123 UniversityService Ave, Tech City', 'Usa', 'George',
        '068ec73a-5210-4891-b4d2-a988541e3854', 1);

--changeset aspeeencinaf:24
INSERT INTO campus (id, name, address, country, city, university_id, version)
VALUES ('5919b48f-1220-46df-b966-52143ef2f995', 'Downtown Campus', '456 City Center Blvd, Tech City', 'Italia',
        'Bologna', '068ec73a-5210-4891-b4d2-a988541e3854', 1);