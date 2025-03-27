package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDate;

public record CourseResponseDto(String courseName, String courseCode, LocalDate startDate, LocalDate endDate,
                                int creditHours,
                                long departmentId, String identifier) {
}


