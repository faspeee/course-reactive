package com.example.stream.spring.courses.reactive.example.model;

import java.time.LocalDateTime;

public record CampusDto(String name, String address, Long universityId, LocalDateTime createdAt,
                        LocalDateTime updatedAt, String country, String city) {
}