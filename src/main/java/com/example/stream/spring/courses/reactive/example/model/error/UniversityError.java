package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface UniversityError extends Error permits UniversityNotFound, UniversityServerError {
}
