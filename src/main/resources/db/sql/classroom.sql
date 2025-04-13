--liquibase formatted sql

--changeset aspeeencinaf:26
INSERT INTO classroom (building_id, room_number, capacity, version)
VALUES (1, '101', 50, 1);
--changeset aspeeencinaf:27
INSERT INTO classroom (building_id, room_number, capacity, version)
VALUES (1, '102', 30, 1);
--changeset aspeeencinaf:28
INSERT INTO classroom (building_id, room_number, capacity, version)
VALUES (2, '201', 100, 1);
--changeset aspeeencinaf:29
INSERT INTO classroom (building_id, room_number, capacity, version)
VALUES (3, '301', 40, 1);