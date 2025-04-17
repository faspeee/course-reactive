package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.StudentConverter;
import com.example.stream.spring.courses.reactive.example.entity.Student;
import com.example.stream.spring.courses.reactive.example.model.request.StudentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import com.example.stream.spring.courses.reactive.example.repository.EnrollmentRepository;
import com.example.stream.spring.courses.reactive.example.repository.StudentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service class for managing student-related operations in a reactive and non-blocking manner.
 * <p>
 * This service provides functionality to create, retrieve, update, and delete student records,
 * as well as retrieve students based on course and instructor associations.
 * It acts as an intermediary between the controller layer and the data access layer (repositories),
 * handling data conversion and business logic.
 * </p>
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    /**
     * Constructs a new {@link StudentService} with all required dependencies.
     *
     * @param studentRepository    the repository for performing CRUD operations on students
     * @param studentConverter     the converter to map between entity and DTO
     * @param enrollmentRepository the repository to fetch enrollment relationships
     * @param courseRepository     the repository to access course data
     */
    public StudentService(StudentRepository studentRepository,
                          StudentConverter studentConverter,
                          EnrollmentRepository enrollmentRepository,
                          CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves all students from the database.
     *
     * @return a {@link Flux} emitting all {@link StudentResponseDto} instances found
     */
    public Flux<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll()
                .map(studentConverter::toDto);
    }

    /**
     * Finds a student entity by its unique identifier.
     *
     * @param studentId the ID of the student as a string
     * @return a {@link Mono} emitting the {@link Student} entity if found
     */
    private Mono<Student> retrieveStudent(String studentId) {
        return studentRepository.findById(UUID.fromString(studentId));
    }

    /**
     * Retrieves a single student DTO by ID.
     *
     * @param studentId the unique identifier of the student
     * @return a {@link Mono} emitting the matching {@link StudentResponseDto}, if found
     */
    public Mono<StudentResponseDto> getStudentById(String studentId) {
        return retrieveStudent(studentId)
                .map(studentConverter::toDto);
    }

    /**
     * Creates a new student record from the given request DTO.
     *
     * @param studentRequestDto the data representing the student to be created
     * @return a {@link Mono} emitting the saved student in DTO format
     */
    public Mono<StudentResponseDto> createStudent(StudentRequestDto studentRequestDto) {
        return studentRepository.save(studentConverter.toEntity(studentRequestDto))
                .map(studentConverter::toDto);
    }

    /**
     * Applies changes from the request DTO to an existing student entity.
     *
     * @param student           the existing {@link Student} to be updated
     * @param studentRequestDto the incoming updated data
     * @return the modified {@link Student} entity
     */
    private Student updateStudent(Student student, StudentRequestDto studentRequestDto) {
        student.setName(studentRequestDto.name());
        student.setSurname(studentRequestDto.surname());
        student.setEmail(studentRequestDto.email());
        student.setFreshman(studentRequestDto.freshman());
        return student;
    }

    /**
     * Updates an existing student with new values.
     *
     * @param studentId         the ID of the student to update
     * @param studentRequestDto the new data for the student
     * @return a {@link Mono} emitting the updated {@link StudentResponseDto}
     */
    public Mono<StudentResponseDto> updateStudent(String studentId, StudentRequestDto studentRequestDto) {
        return retrieveStudent(studentId)
                .flatMap(student -> studentRepository.save(updateStudent(student, studentRequestDto))
                        .map(studentConverter::toDto));
    }

    /**
     * Checks if a student exists in the system by ID.
     *
     * @param studentId the ID of the student to check
     * @return a {@link Mono} emitting true if found, otherwise false
     */
    private Mono<Boolean> existsStudent(String studentId) {
        return studentRepository.existsById(UUID.fromString(studentId));
    }

    /**
     * Deletes a student by ID, returning true if deletion was successful.
     *
     * @param studentId the ID of the student to delete
     * @return a {@link Mono} emitting true if the student was deleted, false otherwise
     */
    public Mono<Boolean> deleteStudent(String studentId) {
        return existsStudent(studentId)
                .filter(isPresent -> isPresent)
                .switchIfEmpty(Mono.empty())
                .flatMap(present -> studentRepository.deleteById(UUID.fromString(studentId))
                        .then(Mono.just(true)));
    }

    /**
     * Retrieves all students enrolled in a specific course.
     *
     * @param courseId the ID of the course
     * @return a {@link Flux} emitting {@link StudentResponseDto} instances of students enrolled in the course
     */
    public Flux<StudentResponseDto> getStudentsByCourseId(String courseId) {
        return enrollmentRepository.findByCourseId(UUID.fromString(courseId))
                .flatMap(enrollment -> studentRepository.findById(enrollment.getStudentId())
                        .map(studentConverter::toDto));
    }

    /**
     * Retrieves all students taught by a specific instructor.
     * <p>
     * This method traverses from instructor → courses → enrollments → students.
     * It ensures that only unique students are returned using {@code distinct()}.
     * </p>
     *
     * @param teacherId the ID of the instructor
     * @return a {@link Flux} emitting distinct {@link StudentResponseDto} instances taught by the given teacher
     */
    public Flux<StudentResponseDto> getStudentsByTeacherId(String teacherId) {
        return courseRepository.findByInstructorId(UUID.fromString(teacherId))
                .flatMap(course -> enrollmentRepository.findByCourseId(course.getId()))
                .flatMap(enrollment -> studentRepository.findById(enrollment.getStudentId()))
                .map(studentConverter::toDto)
                .distinct();
    }

}
