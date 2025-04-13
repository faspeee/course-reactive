--liquibase formatted sql

--changeset aspeeencinaf:46
INSERT INTO course_prerequisite (course_id, prerequisite_id)
VALUES (2, 1); -- Circuit Analysis requires Introduction to Programming as a prerequisite