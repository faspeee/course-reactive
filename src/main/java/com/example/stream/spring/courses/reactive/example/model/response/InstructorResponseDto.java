package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDateTime;

public record InstructorResponseDto(String instructorId, String name, String email, LocalDateTime createdAt,
                                    LocalDateTime updatedAt) {
}