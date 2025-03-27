package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.Converter;
import com.example.stream.spring.courses.reactive.example.converter.CourseConverter;
import com.example.stream.spring.courses.reactive.example.entity.Course;
import com.example.stream.spring.courses.reactive.example.entity.Student;
import com.example.stream.spring.courses.reactive.example.model.request.CourseDto;
import com.example.stream.spring.courses.reactive.example.model.request.StudentDto;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import com.example.stream.spring.courses.reactive.example.repository.EnrollmentRepository;
import com.example.stream.spring.courses.reactive.example.repository.StudentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final Converter<CourseDto, Course> converter;
    private final Converter<StudentDto, Student> studentConverter;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository, CourseConverter converter, Converter<StudentDto, Student> studentConverter, StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.converter = converter;
        this.studentConverter = studentConverter;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    /**
     * Retrieves all students associated with the given courseId.
     *
     * @param courseId the ID of the course
     * @return a Flux stream of Student objects
     */
    public Flux<StudentDto> getStudentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId)
                .flatMap(enrollment -> studentRepository.findById(enrollment.getStudentId())
                        .map(studentConverter::toDto));
    }

    public Flux<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .map(converter::toDto);
    }

    public Mono<CourseDto> addCourse(CourseDto courseDto) {
        return courseRepository.save(converter.toEntity(courseDto))
                .map(converter::toDto);
    }

    public Mono<CourseDto> updateCourse(CourseDto courseDto) {
        return courseRepository.save(converter.toEntity(courseDto))
                .map(converter::toDto);
    }

    public Mono<Void> delete(long idCourse) {
        return courseRepository.deleteById(idCourse);
    }

    public Mono<CourseDto> getCourse(long courseId) {
        return courseRepository.findById(courseId)
                .map(converter::toDto);
    }
}

