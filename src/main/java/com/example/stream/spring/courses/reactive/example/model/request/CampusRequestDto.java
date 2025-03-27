package com.example.stream.spring.courses.reactive.example.model.request;

public record CampusRequestDto(String name, String address, Long universityId, String country, String city,
                               String identifier) {
}