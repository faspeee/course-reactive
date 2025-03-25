package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Enrollment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface EnrollmentRepository extends ReactiveCrudRepository<Enrollment, Long> {
    // Custom query to find enrollment records by courseId.
    Flux<Enrollment> findByCourseId(Long courseId);
}
