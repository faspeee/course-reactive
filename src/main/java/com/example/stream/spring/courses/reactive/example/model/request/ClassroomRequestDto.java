package com.example.stream.spring.courses.reactive.example.model.request;

public record ClassroomRequestDto(String buildingId, String roomNumber, Integer capacity, String identifier) {
}