package com.example.stream.spring.courses.reactive.example.model;

import java.time.LocalDateTime;

public record ClassroomDto(Long buildingId, String roomNumber, Integer capacity, LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
}