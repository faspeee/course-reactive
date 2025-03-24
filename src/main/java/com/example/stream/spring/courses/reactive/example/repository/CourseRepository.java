package com.example.stream.spring.courses.reactive.example.repository;

import com.example.stream.spring.courses.reactive.example.entity.Course;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface CourseRepository extends ReactiveCrudRepository<Course, Long> {
}

