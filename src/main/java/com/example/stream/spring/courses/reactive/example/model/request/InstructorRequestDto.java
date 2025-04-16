package com.example.stream.spring.courses.reactive.example.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InstructorRequestDto(String name, @NotBlank(message = "Email is required")
@Email(message = "Email should be valid") String email, String identifier) {
}