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

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter converter;
    private final DepartmentService departmentService;

    public CourseService(CourseRepository courseRepository, CourseConverter converter, DepartmentService departmentService) {
        this.courseRepository = courseRepository;
        this.converter = converter;
        this.departmentService = departmentService;
    }

    private static Course updateCourse(Course course, CourseRequestDto courseRequestDto) {
        course.setCourseCode(courseRequestDto.courseCode());
        course.setCourseName(courseRequestDto.courseName());
        course.setCreditHours(courseRequestDto.creditHours());
        course.setIdentifier(courseRequestDto.identifier());
        course.setDepartmentId(UUID.fromString(courseRequestDto.departmentId()));
        return course;
    }


    public Flux<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll()
                .map(converter::toDto);
    }

    public Mono<Either<Error, CourseResponseDto>> addCourse(CourseRequestDto courseDto) {
        return departmentService.existDepartmentById(courseDto.departmentId())
                .flatMap(either -> either.getRight()
                        .map(result -> courseRepository.save(converter.toEntity(courseDto))
                                .<Either<Error, CourseResponseDto>>map(course -> Either.right(converter.toDto(course))))
                        .orElse(createMonoWithError(either)));
    }

    private Mono<Either<Error, Course>> retrieveCourseById(String courseId) {
        return courseRepository.findById(UUID.fromString(courseId))
                .<Either<Error, Course>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new CourseNotFound())));
    }

    public Mono<Either<Error, CourseResponseDto>> updateCourse(String courseId, CourseRequestDto courseDto) {
        return retrieveCourseById(courseId)
                .flatMap(either -> either.getRight()
                        .map(course -> departmentService.existDepartmentById(courseDto.departmentId())
                                .flatMap(ignored -> courseRepository.save(updateCourse(course, courseDto))
                                        .<Either<Error, CourseResponseDto>>map(course1 -> Either.right(converter.toDto(course1)))))
                        .orElse(createMonoWithError(either)));
    }

    private Mono<Either<Error, Boolean>> existCourseById(String campusId) {
        return courseRepository.existsById(UUID.fromString(campusId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new BuildingNotFound())));
    }

    public Mono<Either<Error, Success>> deleteCourseById(String idCourse) {
        return existCourseById(idCourse)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(building ->
                                courseRepository.deleteById(UUID.fromString(idCourse))
                                        .then(Mono.just(Either.right(new CourseDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }

    public Mono<Either<Error, CourseResponseDto>> getCourseById(String courseId) {
        return retrieveCourseById(courseId)
                .map(errorCourseEither -> errorCourseEither.map(converter::toDto));
    }

    public Flux<CourseResponseDto> getCourseByDepartmentId(String departmentId) {
        return courseRepository.findByDepartmentId(UUID.fromString(departmentId))
                .map(converter::toDto);
    }
}

