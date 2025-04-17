package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.CourseConverter;
import com.example.stream.spring.courses.reactive.example.entity.Course;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.Success;
import com.example.stream.spring.courses.reactive.example.model.request.CourseRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CourseResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import com.example.stream.spring.courses.reactive.example.repository.DepartmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter converter;
    private final DepartmentRepository departmentRepository;

    public CourseService(CourseRepository courseRepository, CourseConverter converter, DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.converter = converter;
        this.departmentRepository = departmentRepository;
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
        return departmentRepository.findById(UUID.fromString(courseDto.departmentId()))
                .flatMap(department -> courseRepository.save(converter.toEntity(courseDto))
                        .map(converter::toDto))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The department is not found")));
    }

    private Mono<Course> retrieveCourseById(String courseId) {
        return courseRepository.findById(UUID.fromString(courseId));
    }

    private Mono<Boolean> existDepartment(String departmentId) {
        return departmentRepository.existsById(UUID.fromString(departmentId));
    }

    public Mono<Either<Error, CourseResponseDto>> updateCourse(String courseId, CourseRequestDto courseDto) {
        return retrieveCourseById(courseId)
                .flatMap(course -> existDepartment(courseDto.departmentId())
                        .filter(departmentExists -> departmentExists)
                        .flatMap(departmentExists -> courseRepository.save(updateCourse(course, courseDto))
                                .map(converter::toDto)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The department is not found")))))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The course is not found")));
    }

    public Mono<Either<Error, Success>> deleteCourseById(String idCourse) {
        return courseRepository.deleteById(UUID.fromString(idCourse));
    }

    public Mono<Either<Error, CourseResponseDto>> getCourseById(String courseId) {
        return retrieveCourseById(courseId)
                .map(converter::toDto);
    }

    public Flux<CourseResponseDto> getCourseByDepartmentId(String departmentId) {
        return courseRepository.findByDepartmentId(UUID.fromString(departmentId))
                .map(converter::toDto);
    }
}

