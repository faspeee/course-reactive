--liquibase formatted sql

--changeset aspeeencinaf:33
INSERT INTO classroom (building_id, room_number, capacity, version)
VALUES (1, '101', 50, 1);
--changeset aspeeencinaf:34
INSERT INTO classroom (building_id, room_number, capacity, version)
VALUES (1, '102', 30, 1);
--changeset aspeeencinaf:35
INSERT INTO classroom (building_id, room_number, capacity, version)
VALUES (2, '201', 100, 1);
--changeset aspeeencinaf:36
INSERT INTO classroom (building_id, room_number, capacity, version)
VALUES (3, '301', 40, 1);