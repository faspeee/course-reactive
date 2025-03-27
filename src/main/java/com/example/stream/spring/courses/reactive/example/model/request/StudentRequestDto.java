package com.example.stream.spring.courses.reactive.example.model.request;

public record StudentRequestDto(Long id, String name, String surname, String email) {
    public StudentRequestDto(String name, String surname, String email) {
        this(null, name, surname, email);
    }
}
