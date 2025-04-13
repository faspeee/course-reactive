--liquibase formatted sql

--changeset aspeeencinaf:1
CREATE EXTENSION IF NOT EXISTS pgcrypto;

--changeset aspeeencinaf:2
DROP ALL OBJECTS;

--changeset aspeeencinaf:3
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

--changeset aspeeencinaf:4
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

--changeset aspeeencinaf:5
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
--changeset aspeeencinaf:6
CREATE TABLE instructor_department
(
    instructor_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    PRIMARY KEY (instructor_id, department_id),
    FOREIGN KEY (instructor_id) REFERENCES instructor (id),
    FOREIGN KEY (department_id) REFERENCES department (id)
);

--changeset aspeeencinaf:7
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

--changeset aspeeencinaf:8
CREATE TABLE enrollment
(
    student_id  BIGINT NOT NULL,
    course_id   BIGINT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student (id),
    FOREIGN KEY (course_id) REFERENCES course (id)
);

--changeset aspeeencinaf:9
CREATE TABLE course_prerequisite
(
    course_id       BIGINT NOT NULL,
    prerequisite_id BIGINT NOT NULL,
    PRIMARY KEY (course_id, prerequisite_id),
    FOREIGN KEY (course_id) REFERENCES course (id),
    FOREIGN KEY (prerequisite_id) REFERENCES course (id)
);

-- UniversityService Table
--changeset aspeeencinaf:10
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
    motto         VARCHAR(255),   -- UniversityService motto or slogan
    colors        VARCHAR(100),   -- Official university colors
    mascot        VARCHAR(100),   -- UniversityService mascot
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
--changeset aspeeencinaf:11
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
--changeset aspeeencinaf:12
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
--changeset aspeeencinaf:13
CREATE TABLE building
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(100)       NOT NULL,
    code       VARCHAR(10) UNIQUE NOT NULL,
    campus_id  BIGINT             NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version    BIGINT,
    FOREIGN KEY (campus_id) REFERENCES campus (id) ON DELETE CASCADE
);

-- Classroom Table
--changeset aspeeencinaf:14
CREATE TABLE classroom
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    building_id BIGINT      NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    capacity    INT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version     BIGINT,
    FOREIGN KEY (building_id) REFERENCES building (id) ON DELETE CASCADE
);