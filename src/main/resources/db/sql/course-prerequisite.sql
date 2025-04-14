--liquibase formatted sql

--changeset aspeeencinaf:54
INSERT INTO course_prerequisite (course_id, prerequisite_id)
VALUES ('7e73ad23-266d-4746-b948-1f78e722cf02',
        'cb3dc0a6-3bab-462d-ae69-2ad134495def'); -- Circuit Analysis requires Introduction to Programming as a prerequisite