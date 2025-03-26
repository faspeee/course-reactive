package com.example.stream.spring.courses.reactive.example.model;

import java.time.LocalDateTime;


public record CollegeDto(String name, String dean, Long universityId, LocalDateTime createdAt,
                         LocalDateTime updatedAt) {
}
