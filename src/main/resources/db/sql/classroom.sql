--liquibase formatted sql

--changeset aspeeencinaf:33
INSERT INTO classroom (id, building_id, room_number, capacity, version)
VALUES ('76aa28de-baa8-4a90-9a7c-46dfa31da5b8', '199d0cb2-be34-48da-8915-9964daf6d724', '101', 50, 1);
--changeset aspeeencinaf:34
INSERT INTO classroom (id, building_id, room_number, capacity, version)
VALUES ('cc668168-5464-4f02-a39a-0374a60d9f4b', '199d0cb2-be34-48da-8915-9964daf6d724', '102', 30, 1);
--changeset aspeeencinaf:35
INSERT INTO classroom (id, building_id, room_number, capacity, version)
VALUES ('968fc101-7ef6-442b-8ab5-4f6e7fac48d8', '3ceaff23-8f6e-4557-9fc6-c294f64063d4', '201', 100, 1);
--changeset aspeeencinaf:36
INSERT INTO classroom (id, building_id, room_number, capacity, version)
VALUES ('ddfb7d3e-c7c8-4495-a1b2-8aa6736f09ca', '4806db08-b4fd-49d9-b099-2b8b79d13259', '301', 40, 1);