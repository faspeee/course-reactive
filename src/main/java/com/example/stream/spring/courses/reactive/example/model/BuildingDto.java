package com.example.stream.spring.courses.reactive.example.model;

import java.time.LocalDateTime;

public record BuildingDto(String name, String code, Long campusId, LocalDateTime createdAt, LocalDateTime updatedAt) {
}