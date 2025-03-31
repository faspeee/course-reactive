package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.University;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UniversityRepository extends ReactiveCrudRepository<University, Long> {
}
