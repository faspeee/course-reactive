DROP ALL OBJECTS;
CREATE TABLE student
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    surname    VARCHAR(100) NOT NULL,
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
    identifier  VARCHAR(50)  NOT NULL,
    college_id  BIGINT       NOT NULL,
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
-- University Table
CREATE TABLE university
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    country       VARCHAR(50)  NOT NULL,
    city          VARCHAR(50)  NOT NULL,
    location      VARCHAR(50)  NOT NULL,
    established   DATE,
    accreditation VARCHAR(100),   -- Accreditation body or status
    president     VARCHAR(100),   -- Current president or chancellor
    student_count INT,            -- Number of enrolled students
    website       VARCHAR(255),   -- Official website URL
    contact_email VARCHAR(100),   -- General contact email address
    phone_number  VARCHAR(20),    -- General contact phone number
    motto         VARCHAR(255),   -- University motto or slogan
    colors        VARCHAR(100),   -- Official university colors
    mascot        VARCHAR(100),   -- University mascot
    campus_area   DECIMAL(10, 2), -- Total campus area in acres or hectares
    num_faculties INT,            -- Number of faculties or schools within the university
    num_programs  INT,            -- Number of academic programs offered
    international BOOLEAN,        -- Indicator if the university has international affiliations
    ranking       INT,            -- National or international ranking position
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version       BIGINT
);
-- Campus Table
CREATE TABLE campus
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    address       VARCHAR(255),
    country       VARCHAR(50)  NOT NULL,
    city          VARCHAR(50)  NOT NULL,
    university_id BIGINT       NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version       BIGINT,
    FOREIGN KEY (university_id) REFERENCES university (id)
);

-- College Table
CREATE TABLE college
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    dean          VARCHAR(100),
    university_id BIGINT       NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version       BIGINT,
    FOREIGN KEY (university_id) REFERENCES university (id)
);

-- Building Table
CREATE TABLE building
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(100)       NOT NULL,
    code       VARCHAR(10) UNIQUE NOT NULL,
    campus_id  BIGINT             NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version    BIGINT,
    FOREIGN KEY (campus_id) REFERENCES campus (id)
);

-- Classroom Table
CREATE TABLE classroom
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    building_id BIGINT      NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    capacity    INT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version     BIGINT,
    FOREIGN KEY (building_id) REFERENCES building (id)
);
INSERT INTO university (name, country, city, location, established, accreditation, president, student_count, website,
                        contact_email, phone_number, motto, colors, mascot, campus_area, num_faculties, num_programs,
                        international, ranking, created_at, updated_at, version)
VALUES ('Springfield University', 'USA', 'Springfield', '742 Evergreen Terrace', '1950-09-15',
        'Higher Learning Commission', 'Dr. Jane Smith', 15000, 'https://www.springfielduniversity.edu',
        'info@springfielduniversity.edu', '+1-555-123-4567', 'Knowledge and Wisdom',
        'Blue and Gold', 'The Fighting Squirrel', 150.75, 10, 85, TRUE, 120, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 1);


INSERT INTO campus (name, address, country, city, university_id, version)
VALUES ('Main Campus', '123 University Ave, Tech City', 'Usa', 'George', 1, 1),
       ('Downtown Campus', '456 City Center Blvd, Tech City', 'Italia', 'Bologna', 1, 1);
INSERT INTO college (name, dean, university_id, version)
VALUES ('College of Engineering', 'Dr. Alan Turing', 1, 1),
       ('College of Arts and Sciences', 'Dr. Marie Curie', 1, 1);
INSERT INTO department (name, description, identifier, college_id, version)
VALUES ('Computer Science', 'Focuses on the study of computer systems and computational processes.', 'DSUEOW823', 1, 1),
       ('Electrical Engineering',
        'Deals with the study and application of electricity, electronics, and electromagnetism.', 'SWPSAS83', 1, 1),
       ('Biology', 'Explores the science of life and living organisms.', 'SWPSAS81', 2, 1);
INSERT INTO building (name, code, campus_id, version)
VALUES ('Engineering Hall', 'ENGH', 1, 1),
       ('Science Building', 'SCIB', 1, 1),
       ('Downtown Center', 'DTC', 2, 1);
INSERT INTO classroom (building_id, room_number, capacity, version)
VALUES (1, '101', 50, 1),
       (1, '102', 30, 1),
       (2, '201', 100, 1),
       (3, '301', 40, 1);
INSERT INTO instructor (name, email, created_at, updated_at, version)
VALUES ('Dr. Emily Johnson', 'e.johnson@globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
       ('Dr. Michael Brown', 'm.brown@globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
       ('Dr. Sarah Davis', 's.davis@globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO instructor_department (instructor_id, department_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);
INSERT INTO course (course_name, course_code, start_date, end_date, credit_hours, description, instructor_id, is_active,
                    department_id)
VALUES ('Introduction to Programming', 'CS101', '2025-09-01', '2025-12-15', 3,
        'Basic concepts of programming using Python.', 1, TRUE, 1),
       ('Circuit Analysis', 'EE201', '2025-09-01', '2025-12-15', 4,
        'Study of electrical circuits and their components.', 2, TRUE, 2),
       ('General Biology', 'BIO101', '2025-09-01', '2025-12-15', 3,
        'Introduction to biological principles and concepts.', 3, TRUE, 3);
INSERT INTO student (name, surname, email, created_at, updated_at, freshman, version)
VALUES ('Alice', 'Williams', 'alice.williams@student.globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1),
       ('Bob', 'Miller', 'bob.miller@student.globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1),
       ('Charlie', 'Garcia', 'charlie.garcia@student.globaltech.edu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Yes', 1);
INSERT INTO enrollment (student_id, course_id, enrolled_at)
VALUES (1, 1, CURRENT_TIMESTAMP),
       (2, 2, CURRENT_TIMESTAMP),
       (3, 3, CURRENT_TIMESTAMP),
       (1, 2, CURRENT_TIMESTAMP),
       (2, 3, CURRENT_TIMESTAMP);
INSERT INTO course_prerequisite (course_id, prerequisite_id)
VALUES (2, 1); -- Circuit Analysis requires Introduction to Programming as a prerequisite

