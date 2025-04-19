package com.example.stream.spring.courses.reactive.example.utility;

import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Optional;

public final class ProcessResponses {
    private ProcessResponses() {
    }

    public static Mono<Void> processEmptyResponse(Mono<Either<Error, Success>> building) {
        return building
                .flatMap(errorBuildingResponseDtoEither ->
                        switch (errorBuildingResponseDtoEither) {
                            case Either.Left<Error, Success> left ->
                                    checkLeft(left.getLeft().orElse(new GenericError()));
                            case Either.Right<Error, Success> ignored -> Mono.empty();
                        });
    }

    public static <T> Mono<Optional<T>> processTheResultFromService(Mono<Either<Error, T>> building) {
        return building
                .flatMap(errorBuildingResponseDtoEither ->
                        switch (errorBuildingResponseDtoEither) {
                            case Either.Left<Error, T> left -> checkLeft(left.getLeft().orElse(new GenericError()));
                            case Either.Right<Error, T> right -> Mono.just(right.getRight());
                        });
    }

    private static <T> Mono<T> handlerBuildingError(Error error) {
        return switch (error) {
            case BuildingNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Building not found");
            case BuildingServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Building internal error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "The request contains an error");
        };
    }

    private static <T> Mono<T> handlerCampusError(Error error) {
        return switch (error) {
            case CampusNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Campus not found");
            case CampusServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Campus internal error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "The request contains an error");
        };
    }

    private static <T> Mono<T> handlerClassroomError(Error error) {
        return switch (error) {
            case ClassroomNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Classroom not found");
            case ClassroomServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Classroom internal error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "The request contains an error");
        };
    }

    private static <T> Mono<T> createMonoError(HttpStatus httpStatus, String message) {
        return Mono.error(new ResponseStatusException(httpStatus, message));
    }

    private static <T> Mono<T> handlerCollegeError(Error error) {
        return switch (error) {
            case CollegeNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "College not found");
            case CollegeServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "College internal error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "The request contains an error");
        };
    }

    private static <T> Mono<T> handlerCourseError(Error error) {
        return switch (error) {
            case CollegeNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Course not found");
            case CourseServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Course internal error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "The request contains an error");
        };
    }

    private static <T> Mono<T> handlerDepartmentError(Error error) {
        return switch (error) {
            case DepartmentNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Department not found");
            case DepartmentServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Department internal error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "The request contains an error");
        };
    }

    private static <T> Mono<T> handlerInstructorError(Error error) {
        return switch (error) {
            case InstructorNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Instructor not found");
            case InstructorServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Instructor internal error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "The request contains an error");
        };
    }

    private static <T> Mono<T> handlerStudentError(Error error) {
        return switch (error) {
            case StudentNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "Student not found");
            case StudentServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Student internal error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "The request contains an error");
        };
    }

    private static <T> Mono<T> handlerUniversityError(Error error) {
        return switch (error) {
            case UniversityNotFound ignored -> createMonoError(HttpStatus.NOT_FOUND, "University not found");
            case UniversityServerError ignored ->
                    createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "University internal error");
            default -> createMonoError(HttpStatus.BAD_REQUEST, "The request contains an error");
        };
    }

    private static <T> Mono<T> checkLeft(Error error) {
        return switch (error) {
            case BuildingError buildingError -> handlerBuildingError(buildingError);
            case CampusError campusError -> handlerCampusError(campusError);
            case ClassroomError classroomError -> handlerClassroomError(classroomError);
            case CollegeError collegeError -> handlerCollegeError(collegeError);
            case CourseError courseError -> handlerCourseError(courseError);
            case DepartmentError departmentError -> handlerDepartmentError(departmentError);
            case InstructorError instructorError -> handlerInstructorError(instructorError);
            case StudentError studentError -> handlerStudentError(studentError);
            case UniversityError universityError -> handlerUniversityError(universityError);
            case GenericError ignored -> createMonoError(HttpStatus.INTERNAL_SERVER_ERROR, "Generic error find");
        };
    }
}
