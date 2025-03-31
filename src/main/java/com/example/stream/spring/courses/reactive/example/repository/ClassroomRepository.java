package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Classroom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ClassroomRepository extends ReactiveCrudRepository<Classroom, Long> {
}
