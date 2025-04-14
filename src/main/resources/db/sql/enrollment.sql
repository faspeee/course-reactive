--liquibase formatted sql

--changeset aspeeencinaf:49
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES ('1eb7de5d-746c-493b-865e-b524d7e65d49', 'cb3dc0a6-3bab-462d-ae69-2ad134495def', CURRENT_TIMESTAMP);
--changeset aspeeencinaf:50
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES ('23aee4d8-41a5-44ed-9fe7-167aea20066e', '7e73ad23-266d-4746-b948-1f78e722cf02', CURRENT_TIMESTAMP);
--changeset aspeeencinaf:51
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES ('2853e3c3-df5f-4e35-86ff-88b749183b7a', '7475417a-f970-4366-aa9f-54c7bb66fe0a', CURRENT_TIMESTAMP);
--changeset aspeeencinaf:52
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES ('1eb7de5d-746c-493b-865e-b524d7e65d49', '7e73ad23-266d-4746-b948-1f78e722cf02', CURRENT_TIMESTAMP);
--changeset aspeeencinaf:53
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES ('23aee4d8-41a5-44ed-9fe7-167aea20066e', '7475417a-f970-4366-aa9f-54c7bb66fe0a', CURRENT_TIMESTAMP);