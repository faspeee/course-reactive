package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Department;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface DepartmentRepository extends ReactiveCrudRepository<Department, UUID> {
}
