package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.CourseConverter;
import com.example.stream.spring.courses.reactive.example.converter.StudentConverter;
import com.example.stream.spring.courses.reactive.example.entity.Course;
import com.example.stream.spring.courses.reactive.example.model.request.CourseRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CourseResponseDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import com.example.stream.spring.courses.reactive.example.repository.DepartmentRepository;
import com.example.stream.spring.courses.reactive.example.repository.EnrollmentRepository;
import com.example.stream.spring.courses.reactive.example.repository.StudentRepository;
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
    private final StudentConverter studentConverter;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final DepartmentRepository departmentRepository;

    public CourseService(CourseRepository courseRepository, CourseConverter converter, StudentConverter studentConverter,
                         StudentRepository studentRepository, EnrollmentRepository enrollmentRepository, DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.converter = converter;
        this.studentConverter = studentConverter;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
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

    public Flux<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll()
                .map(converter::toDto);
    }

    public Mono<CourseResponseDto> addCourse(CourseRequestDto courseDto) {
        return departmentRepository.findById(UUID.fromString(courseDto.departmentId()))
                .flatMap(department -> courseRepository.save(converter.toEntity(courseDto))
                        .map(converter::toDto))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The department is not found")));
    }

    private Mono<Course> getCourseById(String courseId) {
        return courseRepository.findById(UUID.fromString(courseId));
    }

    private Mono<Boolean> existDepartment(String departmentId) {
        return departmentRepository.existsById(UUID.fromString(departmentId));
    }

    public Mono<CourseResponseDto> updateCourse(String courseId, CourseRequestDto courseDto) {
        return getCourseById(courseId)
                .flatMap(course -> existDepartment(courseDto.departmentId())
                        .filter(departmentExists -> departmentExists)
                        .flatMap(departmentExists -> courseRepository.save(updateCourse(course, courseDto))
                                .map(converter::toDto)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The department is not found")))))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The course is not found")));
    }

    public Mono<Void> delete(String idCourse) {
        return courseRepository.deleteById(UUID.fromString(idCourse));
    }

    public Mono<CourseResponseDto> getCourse(String courseId) {
        return getCourseById(courseId)
                .map(converter::toDto);
    }
}

