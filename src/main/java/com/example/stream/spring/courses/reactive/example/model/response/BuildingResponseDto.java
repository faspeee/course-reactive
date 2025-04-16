package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDateTime;

public record BuildingResponseDto(String buildingId, String name, String code, String campusId,
                                  LocalDateTime createdAt, LocalDateTime updatedAt,
                                  String identifier) {
}