package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface InstructorError extends Error permits InstructorNotFound, InstructorServerError {
}
