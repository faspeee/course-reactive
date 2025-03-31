package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.College;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CollegeRepository extends ReactiveCrudRepository<College, Long> {
}
