package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CampusResponseDto(String campusId, String name, String address, Long universityId, String country,
                                String city, LocalDate startDate, LocalDate endDate, LocalDateTime createdAt,
                                LocalDateTime updatedAt, String identifier) {
}