package com.example.stream.spring.courses.reactive.example.model.request;

import java.time.LocalDateTime;

public record InstructorRequestDto(String name, String email, LocalDateTime createdAt,
                                   LocalDateTime updatedAt, String identifier) {
}