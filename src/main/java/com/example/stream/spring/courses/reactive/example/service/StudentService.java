package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.StudentConverter;
import com.example.stream.spring.courses.reactive.example.entity.Student;
import com.example.stream.spring.courses.reactive.example.model.request.StudentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import com.example.stream.spring.courses.reactive.example.repository.EnrollmentRepository;
import com.example.stream.spring.courses.reactive.example.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository, StudentConverter studentConverter, EnrollmentRepository enrollmentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    public Flux<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll()
                .map(studentConverter::toDto);
    }

    public Mono<StudentResponseDto> getStudentById(String studentId) {
        return studentRepository.findById(UUID.fromString(studentId))
                .map(studentConverter::toDto);
    }

    public Mono<StudentResponseDto> createStudent(StudentRequestDto studentRequestDto) {
        return studentRepository.save(studentConverter.toEntity(studentRequestDto))
                .map(studentConverter::toDto);
    }

    private Student updateStudent(Student student, StudentRequestDto studentRequestDto) {
        student.setName(studentRequestDto.name());
        student.setSurname(studentRequestDto.surname());
        student.setEmail(studentRequestDto.email());
        student.setFreshman(studentRequestDto.freshman());
        return student;
    }

    public Mono<StudentResponseDto> updateStudent(String studentId, StudentRequestDto studentRequestDto) {
        return studentRepository.findById(UUID.fromString(studentId))
                .flatMap(student -> studentRepository.save(updateStudent(student, studentRequestDto))
                        .map(studentConverter::toDto))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "student not found")));
    }

    public Mono<Void> deleteStudent(String studentId) {
        return studentRepository.deleteById(UUID.fromString(studentId));
    }

    /**
     * Retrieves all students associated with the given courseId.
     *
     * @param courseId the ID of the course
     * @return a Flux stream of Student objects
     */
    public Flux<StudentResponseDto> getStudentsByCourseId(String courseId) {
        return enrollmentRepository.findByCourseId(UUID.fromString(courseId))
                .flatMap(enrollment -> studentRepository.findById(enrollment.getStudentId())
                        .map(studentConverter::toDto));
    }

    /**
     * Retrieves all students associated with a specific teacher.
     *
     * @param teacherId the ID of the teacher
     * @return a Flux containing StudentDTOs of the associated students
     */
    public Flux<StudentResponseDto> getStudentsByTeacherId(String teacherId) {
        return courseRepository.findByInstructorId(UUID.fromString(teacherId))
                .flatMap(course -> enrollmentRepository.findByCourseId(course.getId()))
                .flatMap(enrollment -> studentRepository.findById(enrollment.getStudentId()))
                .map(studentConverter::toDto)
                .distinct();
    }

}
