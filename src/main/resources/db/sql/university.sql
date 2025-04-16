--liquibase formatted sql

--changeset aspeeencinaf:22
INSERT INTO university (name, country, city, location, established, accreditation, president, student_count, website,
                        contact_email, phone_number, motto, colors, mascot, campus_area, num_faculties, num_programs,
                        international, ranking, created_at, updated_at, version)
VALUES ('Springfield UniversityService', 'USA', 'Springfield', '742 Evergreen Terrace', '1950-09-15',
        'Higher Learning Commission', 'Dr. Jane Smith', 15000, 'https://www.springfielduniversity.edu',
        'info@springfielduniversity.edu', '+1-555-123-4567', 'Knowledge and Wisdom',
        'Blue and Gold', 'The Fighting Squirrel', 150.75, 10, 85, TRUE, 120, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP, 1);