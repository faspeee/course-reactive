package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDateTime;

public record CampusResponseDto(String name, String address, Long universityId, String country, String city,
                                LocalDateTime createdAt, LocalDateTime updatedAt, String identifier) {
}