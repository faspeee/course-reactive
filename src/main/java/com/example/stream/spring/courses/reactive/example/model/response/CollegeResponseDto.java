package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDateTime;


public record CollegeResponseDto(String collegeId, String name, String dean, Long universityId, LocalDateTime createdAt,
                                 LocalDateTime updatedAt, String identifier) {
}
