package com.example.stream.spring.courses.reactive.example.utility;

import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * {@code ProcessResponses} is a centralized utility class designed to handle service-layer responses
 * encapsulated in {@link Either} structures within a reactive programming context using Project Reactor's {@link Mono}.
 * <p>
 * This class decouples business logic from HTTP response management by:
 * <ul>
 *     <li>Mapping {@link Either.Left} (errors) to proper HTTP responses via {@link ResponseStatusException}.</li>
 *     <li>Extracting {@link Either.Right} values and forwarding them as successful results.</li>
 *     <li>Offering a consistent strategy to transform domain-level errors (e.g., {@code BuildingNotFound})
 *         into appropriate HTTP-level semantics (e.g., 404 NOT FOUND).</li>
 * </ul>
 *
 * <p>This utility is a key part of a functional error handling strategy using the {@code Either<Error, T>} pattern.
 * It ensures that all controller-level response conversions are standardized and domain-specific exceptions are
 * transparently propagated.</p>
 *
 * <p><strong>Usage examples:</strong></p>
 *
 * <pre>{@code
 * // Service method
 * Mono<Either<Error, Success>> result = buildingService.deleteBuilding("1234");
 *
 * // Controller handler
 * return ProcessResponses.processEmptyResponse(result);
 * }</pre>
 */
public final class ProcessResponses {

    /**
     * Private constructor to enforce static-only usage.
     */
    private ProcessResponses() {
    }

    /**
     * Processes a {@link Mono} containing an {@code Either<Error, Success>} from a service layer operation.
     * This method is ideal for "write-only" actions such as deletes or updates, where no response body is required.
     *
     * <p>If the response is {@link Either.Right}, it completes successfully and returns an empty {@code Mono<Void>}.
     * If it's an {@link Either.Left}, the error is translated to an HTTP error using {@link ResponseStatusException},
     * with appropriate status codes like 404, 500, etc.</p>
     *
     * @param response a {@code Mono<Either<Error, Success>>} representing the result of a service operation
     * @return a {@code Mono<Void>} that completes normally or terminates with an HTTP exception
     */
    public static Mono<Void> processEmptyResponse(Mono<Either<Error, Success>> response) {
        return response.flatMap(either ->
                switch (either) {
                    case Either.Left<Error, Success> left -> checkLeft(left.getLeft().orElse(new GenericError()));
                    case Either.Right<Error, Success> ignored -> Mono.empty();
                });
    }

    /**
     * Processes a {@link Mono} of {@code Either<Error, T>} returned from a service and unwraps its value.
     * Useful for read operations (e.g., find by ID, query) where the result is returned to the client.
     *
     * <p>If the {@link Either} is {@code Right}, it returns the contained value wrapped in {@link Optional}.
     * If the {@link Either} is {@code Left}, the contained error is mapped to a corresponding HTTP exception
     * and emitted as a failure signal.</p>
     *
     * @param response the {@code Mono<Either<Error, T>>} from the service layer
     * @param <T>      the type of the successful value
     * @return a {@code Mono<Optional<T>>} containing the result or emitting a web exception on failure
     */
    public static <T> Mono<Optional<T>> processTheResultFromService(Mono<Either<Error, T>> response) {
        return response.flatMap(either ->
                switch (either) {
                    case Either.Left<Error, T> left -> checkLeft(left.getLeft().orElse(new GenericError()));
                    case Either.Right<Error, T> right -> Mono.just(right.getRight());
                });
    }

    /**
     * Routes domain-specific {@link Error} instances to the appropriate handler method.
     * Each domain error handler maps internal error types to {@link HttpStatus} codes.
     *
     * <p>This design supports domain-oriented exception handling, encapsulating all mapping logic in one place.
     * This makes it easier to extend the system with new domains and error types.</p>
     *
     * @param error the domain-level {@link Error} to handle
     * @param <T>   generic type parameter to support reactive type inference
     * @return a {@code Mono<T>} that emits a {@link ResponseStatusException}
     */
    private static <T> Mono<T> checkLeft(Error error) {
        return switch (error) {
            case BuildingError e -> handlerBuildingError(e);
            case CampusError e -> handlerCampusError(e);
            case ClassroomError e -> handlerClassroomError(e);
            case CollegeError e -> handlerCollegeError(e);
            case CourseError e -> handlerCourseError(e);
            case DepartmentError e -> handlerDepartmentError(e);
            case InstructorError e -> handlerInstructorError(e);
            case StudentError e -> handlerStudentError(e);
            case UniversityError e -> handlerUniversityError(e);
            case GenericError ignored -> createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown error occurred");
        };
    }

    // === Domain-specific error handling ===

    /**
     * Handles errors related to {@code Building} domain.
     */
    private static <T> Mono<T> handlerBuildingError(Error error) {
        return switch (error) {
            case BuildingNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Building not found");
            case BuildingServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected building error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "Building request invalid");
        };
    }

    private static <T> Mono<T> handlerCampusError(Error error) {
        return switch (error) {
            case CampusNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Campus not found");
            case CampusServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected campus error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "Campus request invalid");
        };
    }

    private static <T> Mono<T> handlerClassroomError(Error error) {
        return switch (error) {
            case ClassroomNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Classroom not found");
            case ClassroomServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected classroom error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "Classroom request invalid");
        };
    }

    private static <T> Mono<T> handlerCollegeError(Error error) {
        return switch (error) {
            case CollegeNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "College not found");
            case CollegeServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected college error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "College request invalid");
        };
    }

    private static <T> Mono<T> handlerCourseError(Error error) {
        return switch (error) {
            case CollegeNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Course not found");
            case CourseServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected course error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "Course request invalid");
        };
    }

    private static <T> Mono<T> handlerDepartmentError(Error error) {
        return switch (error) {
            case DepartmentNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Department not found");
            case DepartmentServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected department error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "Department request invalid");
        };
    }

    private static <T> Mono<T> handlerInstructorError(Error error) {
        return switch (error) {
            case InstructorNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Instructor not found");
            case InstructorServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected instructor error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "Instructor request invalid");
        };
    }

    private static <T> Mono<T> handlerStudentError(Error error) {
        return switch (error) {
            case StudentNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Student not found");
            case StudentServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected student error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "Student request invalid");
        };
    }

    private static <T> Mono<T> handlerUniversityError(Error error) {
        return switch (error) {
            case UniversityNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "University not found");
            case UniversityServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected university error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "University request invalid");
        };
    }

    /**
     * Generic helper method that wraps a {@link ResponseStatusException} in a {@code Mono.error()}.
     *
     * @param httpStatus the HTTP status to associate with the error
     * @param message    a human-readable message for the client
     * @param <T>        the expected generic type
     * @return a {@code Mono<T>} that emits a failure with the specified exception
     */
    private static <T> Mono<T> createMonoError(HttpStatus httpStatus, String message) {
        return Mono.error(new ResponseStatusException(httpStatus, message));
    }
}
