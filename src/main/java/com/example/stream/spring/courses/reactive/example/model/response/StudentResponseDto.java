package com.example.stream.spring.courses.reactive.example.model.response;

public record StudentResponseDto(String studentId, String name, String surname, String email) {
    public StudentResponseDto(String name, String surname, String email) {
        this(null, name, surname, email);
    }
}
