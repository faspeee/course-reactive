package com.example.stream.spring.courses.reactive.example.model.request;

public record ClassroomRequestDto(Long buildingId, String roomNumber, Integer capacity, String identifier) {
}