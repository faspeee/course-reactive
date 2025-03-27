package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.CourseConverter;
import com.example.stream.spring.courses.reactive.example.converter.StudentConverter;
import com.example.stream.spring.courses.reactive.example.model.request.CourseRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CourseResponseDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import com.example.stream.spring.courses.reactive.example.repository.EnrollmentRepository;
import com.example.stream.spring.courses.reactive.example.repository.StudentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter converter;
    private final StudentConverter studentConverter;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository, CourseConverter converter, StudentConverter studentConverter,
                         StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
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
    public Flux<StudentResponseDto> getStudentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId)
                .flatMap(enrollment -> studentRepository.findById(enrollment.getStudentId())
                        .map(studentConverter::toDto));
    }

    public Flux<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll()
                .map(converter::toDto);
    }

    public Mono<CourseResponseDto> addCourse(CourseRequestDto courseDto) {
        return courseRepository.save(converter.toEntity(courseDto))
                .map(converter::toDto);
    }

    public Mono<CourseResponseDto> updateCourse(CourseRequestDto courseDto) {
        return courseRepository.save(converter.toEntity(courseDto))
                .map(converter::toDto);
    }

    public Mono<Void> delete(long idCourse) {
        return courseRepository.deleteById(idCourse);
    }

    public Mono<CourseResponseDto> getCourse(long courseId) {
        return courseRepository.findById(courseId)
                .map(converter::toDto);
    }
}

