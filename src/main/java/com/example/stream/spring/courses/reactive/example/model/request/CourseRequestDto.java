package com.example.stream.spring.courses.reactive.example.model.request;

public record CourseRequestDto(String courseName, String courseCode, int creditHours, String departmentId,
                               String identifier) {
}


