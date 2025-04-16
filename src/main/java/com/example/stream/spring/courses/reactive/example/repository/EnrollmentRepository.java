package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Enrollment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface EnrollmentRepository extends ReactiveCrudRepository<Enrollment, UUID> {
    // Custom query to find enrollment records by courseId.
    Flux<Enrollment> findByCourseId(UUID courseId);
}
