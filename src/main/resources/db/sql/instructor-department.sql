--liquibase formatted sql

--changeset aspeeencinaf:40
INSERT INTO instructor_department (instructor_id, department_id)
VALUES ('520ee975-1224-42e6-8255-a9712e2bc22c', '88af4487-6120-4a7f-81fe-a9ebc1072413');
--changeset aspeeencinaf:41
INSERT INTO instructor_department (instructor_id, department_id)
VALUES ('849fdd9f-2535-42f4-9c03-deccde3a0ba4', '2ecc43cc-8ee8-41ab-b4de-26858b533a0f');
--changeset aspeeencinaf:42
INSERT INTO instructor_department (instructor_id, department_id)
VALUES ('705ac9e9-f37a-4d13-9a93-e933346c2e7f', '1a6c2f08-8ee8-438f-b37f-01a1c862efe7');