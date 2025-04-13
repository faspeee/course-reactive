package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Course;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;


public interface CourseRepository extends ReactiveCrudRepository<Course, UUID> {
    Flux<Course> findByInstructorId(UUID teacherId);

}

