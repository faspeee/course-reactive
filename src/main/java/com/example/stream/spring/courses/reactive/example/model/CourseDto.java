package com.example.stream.spring.courses.reactive.example.model;

import java.time.LocalDate;

public record CourseDto(String courseName, String courseCode, LocalDate startDate, LocalDate endDate, int creditHours,
                        long departmentId) {
}


