package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface CampusError extends Error permits CampusNotFound, CampusServerError {
}
