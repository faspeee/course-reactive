package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface CourseError extends Error permits CourseNotFound {
}
