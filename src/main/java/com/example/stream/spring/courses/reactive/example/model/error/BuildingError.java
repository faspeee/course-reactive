package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface BuildingError extends Error permits BuildingNotFound {
}
