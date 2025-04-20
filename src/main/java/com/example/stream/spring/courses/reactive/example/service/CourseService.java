package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.CourseConverter;
import com.example.stream.spring.courses.reactive.example.entity.Course;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.*;
import com.example.stream.spring.courses.reactive.example.model.request.CourseRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CourseResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.stream.spring.courses.reactive.example.utility.UtilMono.createMonoWithError;

/**
 * {@code CourseService} encapsulates the business logic for managing {@link Course} entities.
 * <p>
 * This service provides operations to create, update, delete, and query courses,
 * leveraging a non-blocking, reactive stack using {@link Reactor}'s {@link Mono} and {@link Flux}.
 * </p>
 *
 * <p>It follows a functional error-handling pattern using {@code Either<Error, T>} to model success or domain-specific failure.
 * This approach avoids exception throwing, simplifies flow control, and enhances testability and composability.</p>
 *
 * <p>{@link DepartmentService} is used to validate relationships between courses and departments,
 * enforcing data consistency when a course is associated with a department.</p>
 */
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter converter;
    private final DepartmentService departmentService;

    /**
     * Constructs a new {@code CourseService}.
     *
     * @param courseRepository  the reactive repository interface for accessing course data
     * @param converter         converter to translate between entities and DTOs
     * @param departmentService service used to validate department relationships
     */
    public CourseService(CourseRepository courseRepository, CourseConverter converter, DepartmentService departmentService) {
        this.courseRepository = courseRepository;
        this.converter = converter;
        this.departmentService = departmentService;
    }

    /**
     * Helper method to update the fields of a course based on a DTO.
     * <p>
     * Ensures immutable update logic is centralized and consistent.
     *
     * @param course           the original entity
     * @param courseRequestDto the request data
     * @return the updated {@link Course} entity
     */
    private static Course updateCourse(Course course, CourseRequestDto courseRequestDto) {
        course.setCourseCode(courseRequestDto.courseCode());
        course.setCourseName(courseRequestDto.courseName());
        course.setCreditHours(courseRequestDto.creditHours());
        course.setIdentifier(courseRequestDto.identifier());
        course.setDepartmentId(UUID.fromString(courseRequestDto.departmentId()));
        return course;
    }

    /**
     * Retrieves all courses from the database as response DTOs.
     *
     * @return a {@link Flux} stream of {@link CourseResponseDto}
     */
    public Flux<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll()
                .map(converter::toDto);
    }

    /**
     * Adds a new course to the system after verifying the associated department exists.
     *
     * @param courseDto the course creation request
     * @return a {@link Mono} containing either an error or the created {@link CourseResponseDto}
     */
    public Mono<Either<Error, CourseResponseDto>> addCourse(CourseRequestDto courseDto) {
        return departmentService.existDepartmentById(courseDto.departmentId())
                .flatMap(either -> either.getRight()
                        .map(result -> courseRepository.save(converter.toEntity(courseDto))
                                .<Either<Error, CourseResponseDto>>map(course -> Either.right(converter.toDto(course))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Retrieves a course by ID and wraps it in an {@code Either} structure.
     *
     * @param courseId the course's UUID string
     * @return a {@link Mono} emitting {@code Either<Error, Course>}
     */
    private Mono<Either<Error, Course>> retrieveCourseById(String courseId) {
        return courseRepository.findById(UUID.fromString(courseId))
                .<Either<Error, Course>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new CourseNotFound())));
    }

    /**
     * Updates an existing course if both the course and associated department exist.
     *
     * @param courseId  the course ID
     * @param courseDto updated course request
     * @return a {@link Mono} containing the updated course response or an error
     */
    public Mono<Either<Error, CourseResponseDto>> updateCourse(String courseId, CourseRequestDto courseDto) {
        return retrieveCourseById(courseId)
                .flatMap(either -> either.getRight()
                        .map(course -> departmentService.existDepartmentById(courseDto.departmentId())
                                .flatMap(ignored -> courseRepository.save(updateCourse(course, courseDto))
                                        .<Either<Error, CourseResponseDto>>map(course1 -> Either.right(converter.toDto(course1)))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Checks if a course exists by its ID.
     *
     * @param courseId the course UUID string
     * @return a {@code Mono<Either<Error, Boolean>>}, {@code Right(true)} if exists, otherwise {@code Left}
     */
    private Mono<Either<Error, Boolean>> existCourseById(String courseId) {
        return courseRepository.existsById(UUID.fromString(courseId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new BuildingNotFound()))); // Potential typo: should probably be CourseNotFound
    }

    /**
     * Deletes a course by ID if it exists.
     *
     * @param idCourse the ID of the course to delete
     * @return a {@link Mono} with either success or an error
     */
    public Mono<Either<Error, Success>> deleteCourseById(String idCourse) {
        return existCourseById(idCourse)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(exists ->
                                courseRepository.deleteById(UUID.fromString(idCourse))
                                        .then(Mono.just(Either.right(new CourseDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Retrieves a course by its ID and returns the corresponding response DTO.
     *
     * @param courseId the course ID
     * @return a {@link Mono} emitting {@code Either<Error, CourseResponseDto>}
     */
    public Mono<Either<Error, CourseResponseDto>> getCourseById(String courseId) {
        return retrieveCourseById(courseId)
                .map(errorCourseEither -> errorCourseEither.map(converter::toDto));
    }

    /**
     * Returns all courses associated with a given department.
     *
     * @param departmentId the department UUID
     * @return a {@link Flux} of {@link CourseResponseDto}
     */
    public Flux<CourseResponseDto> getCourseByDepartmentId(String departmentId) {
        return courseRepository.findByDepartmentId(UUID.fromString(departmentId))
                .map(converter::toDto);
    }

}
