CREATE TABLE student (
                         id BIGINT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         email VARCHAR(100) NOT NULL,
                         created_at TIMESTAMP,
                         updated_at TIMESTAMP,
                         version BIGINT
);

-- Sample data for students
INSERT INTO student (id, name, email, created_at, updated_at, version) VALUES
                                                                           (1, 'Alice Smith', 'alice@example.com', NOW(), NOW(), 1),
                                                                           (2, 'Bob Johnson', 'bob@example.com', NOW(), NOW(), 1);
CREATE TABLE department (
                            id BIGINT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            description VARCHAR(500),
                            created_at TIMESTAMP,
                            updated_at TIMESTAMP,
                            version BIGINT
);

-- Sample data for departments
INSERT INTO department (id, name, description, created_at, updated_at, version) VALUES
                                                                                    (1, 'Computer Science', 'Department of Computer Science', NOW(), NOW(), 1),
                                                                                    (2, 'Mathematics', 'Department of Mathematics', NOW(), NOW(), 1);

CREATE TABLE course (
                        id BIGINT PRIMARY KEY,
                        course_name VARCHAR(100),
                        course_code VARCHAR(50),
                        start_date DATE,
                        end_date DATE,
                        credit_hours INT,
                        description VARCHAR(500),
                        instructor VARCHAR(100) NOT NULL,  -- could later be linked to the instructor table
                        is_active BOOLEAN DEFAULT TRUE,
                        department_id BIGINT NOT NULL,
                        FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Sample data for courses
INSERT INTO course (id, course_name, course_code, start_date, end_date, credit_hours, description, instructor, is_active, department_id)
VALUES
    (1, 'Introduction to Programming', 'CS101', '2025-01-15', '2025-05-15', 3, 'Basics of programming', 'Dr. Jane Doe', TRUE, 1),
    (2, 'Data Structures', 'CS201', '2025-01-15', '2025-05-15', 3, 'Advanced programming concepts', 'Dr. John Smith', TRUE, 1),
    (3, 'Calculus I', 'MATH101', '2025-01-15', '2025-05-15', 4, 'Differential and integral calculus', 'Dr. Emily Brown', TRUE, 2);
CREATE TABLE enrollment (
                            student_id BIGINT NOT NULL,
                            course_id BIGINT NOT NULL,
                            enrolled_at TIMESTAMP DEFAULT NOW(),
                            PRIMARY KEY (student_id, course_id),
                            FOREIGN KEY (student_id) REFERENCES student(id),
                            FOREIGN KEY (course_id) REFERENCES course(id)
);

-- Sample enrollment data
INSERT INTO enrollment (student_id, course_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 3);

CREATE TABLE course_prerequisite (
                                     course_id BIGINT NOT NULL,
                                     prerequisite_id BIGINT NOT NULL,
                                     PRIMARY KEY (course_id, prerequisite_id),
                                     FOREIGN KEY (course_id) REFERENCES course(id),
                                     FOREIGN KEY (prerequisite_id) REFERENCES course(id)
);

-- Sample data: Course 2 (Data Structures) requires Course 1 (Introduction to Programming)
INSERT INTO course_prerequisite (course_id, prerequisite_id)
VALUES
    (2, 1);
CREATE TABLE instructor (
                            id BIGINT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            email VARCHAR(100) NOT NULL,
                            department_id BIGINT,
                            created_at TIMESTAMP,
                            updated_at TIMESTAMP,
                            version BIGINT,
                            FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Sample instructor data
INSERT INTO instructor (id, name, email, department_id, created_at, updated_at, version)
VALUES
    (1, 'Dr. Jane Doe', 'jane.doe@example.com', 1, NOW(), NOW(), 1),
    (2, 'Dr. John Smith', 'john.smith@example.com', 1, NOW(), NOW(), 1),
    (3, 'Dr. Emily Brown', 'emily.brown@example.com', 2, NOW(), NOW(), 1);
