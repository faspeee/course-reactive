package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface DepartmentError extends Error permits DepartmentNotFound {
}
