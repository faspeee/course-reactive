package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDateTime;

public record ClassroomResponseDto(Long buildingId, String roomNumber, Integer capacity, LocalDateTime createdAt,
                                   LocalDateTime updatedAt, String identifier) {
}