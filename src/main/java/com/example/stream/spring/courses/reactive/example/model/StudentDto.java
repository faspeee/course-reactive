package com.example.stream.spring.courses.reactive.example.model;

public record StudentDto(Long id, String name, String surname, String email) {
    public StudentDto(String name, String surname, String email) {
        this(null, name, surname, email);
    }
}
