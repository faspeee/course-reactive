package com.example.stream.spring.courses.reactive.example.model.request;

import java.time.LocalDate;

public record UniversityRequestDto(String name, String location, LocalDate established, String accreditation,
                                   String president, Integer studentCount, String website, String contactEmail,
                                   String phoneNumber, String motto, String colors, String mascot, Double campusArea,
                                   Integer numFaculties, Integer numPrograms, Boolean international, Integer ranking,
                                   String country, String city, String identifier) {
}

