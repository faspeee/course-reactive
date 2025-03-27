package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Campus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CampusRepository extends ReactiveCrudRepository<Campus, Long> {
}
