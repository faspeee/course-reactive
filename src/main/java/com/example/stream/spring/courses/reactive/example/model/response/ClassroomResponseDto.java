package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDateTime;

public record ClassroomResponseDto(String classroomId, String buildingId, String roomNumber, Integer capacity,
                                   LocalDateTime createdAt, LocalDateTime updatedAt, String identifier) {
}