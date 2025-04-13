package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Student;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface StudentRepository extends ReactiveCrudRepository<Student, UUID> {
    // Additional custom queries (if needed) can be added here.
}
