package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.InstructorConverter;
import com.example.stream.spring.courses.reactive.example.converter.StudentConverter;
import com.example.stream.spring.courses.reactive.example.entity.Instructor;
import com.example.stream.spring.courses.reactive.example.model.request.InstructorRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.InstructorResponseDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import com.example.stream.spring.courses.reactive.example.repository.EnrollmentRepository;
import com.example.stream.spring.courses.reactive.example.repository.InstructorRepository;
import com.example.stream.spring.courses.reactive.example.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service class that provides reactive operations for managing {@link Instructor} entities.
 * It uses Project Reactor's {@link Mono} and {@link Flux} to return asynchronous, non-blocking results.
 * Converts between domain entities and DTOs using the {@code instructorConverter}, and performs persistence
 * through the {@code instructorRepository}.
 */
@Service
public class InstructorService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;
    private final InstructorRepository instructorRepository;
    private final InstructorConverter instructorConverter;

    /**
     * Constructs a TeacherService with the specified repositories and converter.
     *
     * @param courseRepository     the repository for course data
     * @param enrollmentRepository the repository for enrollment data
     * @param studentRepository    the repository for student data
     * @param studentConverter     the converter for transforming Student entities to DTOs
     */
    public InstructorService(CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             StudentConverter studentConverter, InstructorRepository instructorRepository, InstructorConverter instructorConverter) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
        this.instructorRepository = instructorRepository;
        this.instructorConverter = instructorConverter;
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

    /**
     * Retrieves all instructors from the repository and converts them to response DTOs.
     *
     * @return a {@link Flux} stream of {@link InstructorResponseDto}, representing all instructors in the system.
     * The result is reactive and supports non-blocking backpressure-aware consumption.
     */
    public Flux<InstructorResponseDto> getAllInstructor() {
        return instructorRepository.findAll()
                .map(instructorConverter::toDto);
    }

    /**
     * Retrieves an {@link Instructor} entity by its unique identifier.
     * This is a private helper method used internally for reuse in public-facing operations.
     *
     * @param id the {@link UUID} of the instructor to retrieve.
     * @return a {@link Mono} containing the {@link Instructor} if found; otherwise an empty {@link Mono}.
     */
    private Mono<Instructor> getInstructorById(UUID id) {
        return instructorRepository.findById(id);
    }

    /**
     * Retrieves an instructor by ID and returns a DTO suitable for API clients.
     *
     * @param instructorId the string representation of the instructor's {@link UUID}.
     * @return a {@link Mono} containing the {@link InstructorResponseDto} if found; otherwise an empty {@link Mono}.
     */
    public Mono<InstructorResponseDto> getInstructorById(String instructorId) {
        return getInstructorById(UUID.fromString(instructorId))
                .map(instructorConverter::toDto);
    }

    /**
     * Creates a new instructor in the system using the data provided in the request DTO.
     * Converts the DTO to a domain entity, persists it, and returns the created entity as a DTO.
     *
     * @param requestDto the {@link InstructorRequestDto} containing data for the new instructor.
     * @return a {@link Mono} containing the created {@link InstructorResponseDto}.
     */
    public Mono<InstructorResponseDto> createInstructor(InstructorRequestDto requestDto) {
        return instructorRepository.save(instructorConverter.toEntity(requestDto))
                .map(instructorConverter::toDto);
    }

    /**
     * Updates the fields of an existing {@link Instructor} instance using values from a request DTO.
     * This is an internal utility method used before persisting updated entities.
     *
     * @param instructor the existing instructor entity to update.
     * @param requestDto the new values to apply from the update request.
     * @return the updated {@link Instructor} entity (not persisted yet).
     */
    private Instructor updateInstructor(Instructor instructor, InstructorRequestDto requestDto) {
        instructor.setEmail(requestDto.email());
        instructor.setName(requestDto.name());
        return instructor;
    }

    /**
     * Updates an existing instructor by ID with new values provided in a request DTO.
     * If the instructor does not exist, it returns a {@link Mono} that signals an HTTP 404 error.
     *
     * @param instructorId the string representation of the instructor's {@link UUID}.
     * @param requestDto   the {@link InstructorRequestDto} containing updated information.
     * @return a {@link Mono} containing the updated {@link InstructorResponseDto}.
     * Emits a {@link ResponseStatusException} with {@code NOT_FOUND} if the instructor does not exist.
     */
    public Mono<InstructorResponseDto> updateInstructor(String instructorId, InstructorRequestDto requestDto) {
        return getInstructorById(UUID.fromString(instructorId))
                .flatMap(instructor -> instructorRepository.save(updateInstructor(instructor, requestDto))
                        .map(instructorConverter::toDto))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor not found")));
    }

    /**
     * Deletes an instructor from the system based on the provided identifier.
     * <p>
     * Converts the provided string-based ID into a {@link UUID} and delegates the deletion
     * to the {@code instructorRepository}. If the instructor does not exist, the repository
     * may return an empty {@link Mono} or complete silently, depending on its implementation.
     * <p>
     * This operation is non-blocking and returns a {@link Mono<Void>} that completes when
     * the deletion has been performed.
     *
     * @param instructorId the string representation of the instructor's {@link UUID}.
     * @return a {@link Mono<Void>} that completes when the instructor is deleted or
     * if the ID was not found. It may also signal an error if the ID format is invalid
     * or the deletion operation fails.
     */
    public Mono<Void> deleteInstructor(String instructorId) {
        return instructorRepository.deleteById(UUID.fromString(instructorId));
    }
}
