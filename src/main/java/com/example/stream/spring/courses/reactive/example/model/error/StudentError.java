package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface StudentError extends Error permits StudentNotFound {
}
