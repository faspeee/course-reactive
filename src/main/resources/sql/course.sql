DROP ALL OBJECTS;
CREATE TABLE student
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    freshman   VARCHAR(50)  NOT NULL,
    version    BIGINT
);

CREATE TABLE department
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    version     BIGINT
);

CREATE TABLE instructor
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version    BIGINT
);

-- NEW INTERMEDIATE TABLE: Many-to-Many (Instructor <-> Department)
CREATE TABLE instructor_department
(
    instructor_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    PRIMARY KEY (instructor_id, department_id),
    FOREIGN KEY (instructor_id) REFERENCES instructor (id),
    FOREIGN KEY (department_id) REFERENCES department (id)
);

CREATE TABLE course
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_name   VARCHAR(100),
    course_code   VARCHAR(50),
    start_date    DATE,
    end_date      DATE,
    credit_hours  INT,
    description   VARCHAR(500),
    instructor_id BIGINT, -- Now linked to the instructor table
    is_active     BOOLEAN DEFAULT TRUE,
    department_id BIGINT NOT NULL,
    FOREIGN KEY (instructor_id) REFERENCES instructor (id),
    FOREIGN KEY (department_id) REFERENCES department (id)
);

CREATE TABLE enrollment
(
    student_id  BIGINT NOT NULL,
    course_id   BIGINT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student (id),
    FOREIGN KEY (course_id) REFERENCES course (id)
);

CREATE TABLE course_prerequisite
(
    course_id       BIGINT NOT NULL,
    prerequisite_id BIGINT NOT NULL,
    PRIMARY KEY (course_id, prerequisite_id),
    FOREIGN KEY (course_id) REFERENCES course (id),
    FOREIGN KEY (prerequisite_id) REFERENCES course (id)
);

INSERT INTO department (name, description, created_at, updated_at, version)
VALUES ('Computer Science', 'Department of Computer Science', NOW(), NOW(), 1),
       ('Mathematics', 'Department of Mathematics', NOW(), NOW(), 1),
       ('Physics', 'Department of Physics', NOW(), NOW(), 1);
INSERT INTO instructor (name, email, created_at, updated_at, version)
VALUES ('Dr. Alice Smith', 'alice.smith@university.edu', NOW(), NOW(), 1),
       ('Dr. Bob Johnson', 'bob.johnson@university.edu', NOW(), NOW(), 1),
       ('Dr. Carol Williams', 'carol.williams@university.edu', NOW(), NOW(), 1);
INSERT INTO instructor_department (instructor_id, department_id)
VALUES (1, 1), -- Dr. Alice Smith in Computer Science
       (2, 2), -- Dr. Bob Johnson in Mathematics
       (3, 3), -- Dr. Carol Williams in Physics
       (1, 2); -- Dr. Alice Smith also in Mathematics
INSERT INTO course (course_name, course_code, start_date, end_date, credit_hours, description, instructor_id,
                    is_active, department_id)
VALUES ('Introduction to Programming', 'CS101', '2025-09-01', '2025-12-15', 4, 'Basic programming concepts', 1, TRUE,
        1),
       ('Data Structures', 'CS201', '2025-09-01', '2025-12-15', 3, 'In-depth study of data structures', 1, TRUE, 1),
       ('Calculus I', 'MATH101', '2025-09-01', '2025-12-15', 4, 'Differential and integral calculus', 2, TRUE, 2),
       ('Linear Algebra', 'MATH201', '2025-09-01', '2025-12-15', 3, 'Matrix theory and linear algebra', 2, TRUE, 2),
       ('Classical Mechanics', 'PHYS101', '2025-09-01', '2025-12-15', 4, 'Fundamentals of classical mechanics', 3,
        TRUE, 3);

INSERT INTO student (name, email, created_at, updated_at, freshman, version)
VALUES ('John Doe', 'john.doe@student.edu', NOW(), NOW(), '2025', 1),
       ('Jane Smith', 'jane.smith@student.edu', NOW(), NOW(), '2025', 1),
       ('Emily Johnson', 'emily.johnson@student.edu', NOW(), NOW(), '2025', 1),
       ('Michael Brown', 'michael.brown@student.edu', NOW(), NOW(), '2025', 1),
       ('Sarah Davis', 'sarah.davis@student.edu', NOW(), NOW(), '2025', 1);
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (1, 1, NOW()), -- John Doe in Introduction to Programming
       (2, 1, NOW()), -- Jane Smith in Introduction to Programming
       (3, 2, NOW()), -- Emily Johnson in Data Structures
       (4, 3, NOW()), -- Michael Brown in Calculus I
       (5, 4, NOW()); -- Sarah Davis in Linear Algebra
INSERT INTO course_prerequisite (course_id, prerequisite_id)
VALUES (2, 1), -- Data Structures requires Introduction to Programming
       (4, 3); -- Linear Algebra requires Calculus I
