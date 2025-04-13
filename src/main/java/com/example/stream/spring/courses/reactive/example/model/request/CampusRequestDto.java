package com.example.stream.spring.courses.reactive.example.model.request;

import java.time.LocalDateTime;

public record CampusRequestDto(String name, String address, Long universityId, String country, String city,
                               LocalDateTime startDate, LocalDateTime endDate, String identifier) {
}