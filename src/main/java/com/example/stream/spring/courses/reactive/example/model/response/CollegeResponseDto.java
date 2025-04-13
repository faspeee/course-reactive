package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDateTime;


public record CollegeResponseDto(String collegeId, String name, String dean, String universityId,
                                 LocalDateTime createdAt, LocalDateTime updatedAt, String identifier) {
}
