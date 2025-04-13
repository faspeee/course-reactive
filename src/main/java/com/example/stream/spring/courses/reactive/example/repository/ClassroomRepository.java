package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Classroom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ClassroomRepository extends ReactiveCrudRepository<Classroom, UUID> {
}
