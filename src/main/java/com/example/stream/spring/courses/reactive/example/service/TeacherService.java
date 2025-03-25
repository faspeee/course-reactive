package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.Converter;
import com.example.stream.spring.courses.reactive.example.converter.StudentConverter;
import com.example.stream.spring.courses.reactive.example.entity.Student;
import com.example.stream.spring.courses.reactive.example.model.StudentDTO;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import com.example.stream.spring.courses.reactive.example.repository.EnrollmentRepository;
import com.example.stream.spring.courses.reactive.example.repository.StudentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Service class for handling business logic related to teachers.
 */
@Service
public class TeacherService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final Converter<StudentDTO, Student> studentConverter;

    /**
     * Constructs a TeacherService with the specified repositories and converter.
     *
     * @param courseRepository     the repository for course data
     * @param enrollmentRepository the repository for enrollment data
     * @param studentRepository    the repository for student data
     * @param studentConverter     the converter for transforming Student entities to DTOs
     */
    public TeacherService(CourseRepository courseRepository,
                          EnrollmentRepository enrollmentRepository,
                          StudentRepository studentRepository,
                          StudentConverter studentConverter) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
    }

    /**
     * Retrieves all students associated with a specific teacher.
     *
     * @param teacherId the ID of the teacher
     * @return a Flux containing StudentDTOs of the associated students
     */
    public Flux<StudentDTO> getStudentsByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId)
                .flatMap(course -> enrollmentRepository.findByCourseId(course.getId()))
                .flatMap(enrollment -> studentRepository.findById(enrollment.getStudentId()))
                .map(studentConverter::toDto)
                .distinct();
    }
}
