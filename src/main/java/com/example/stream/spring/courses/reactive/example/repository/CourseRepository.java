package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Course;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface CourseRepository extends ReactiveCrudRepository<Course, Long> {
    Flux<Course> findByInstructorId(Long teacherId);

}

