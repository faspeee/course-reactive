package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface ClassroomError extends Error permits ClassroomNotFound {
}
