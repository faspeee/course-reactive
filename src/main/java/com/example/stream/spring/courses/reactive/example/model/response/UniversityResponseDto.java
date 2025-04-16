package com.example.stream.spring.courses.reactive.example.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UniversityResponseDto(String universityId, String name, String location, LocalDate established,
                                    String accreditation, String president, Integer studentCount, String website,
                                    String contactEmail, String phoneNumber, String motto, String colors, String mascot,
                                    Double campusArea, Integer numFaculties, Integer numPrograms, Boolean international,
                                    Integer ranking, LocalDateTime createdAt, LocalDateTime updatedAt, String country,
                                    String city, String identifier) {
}

