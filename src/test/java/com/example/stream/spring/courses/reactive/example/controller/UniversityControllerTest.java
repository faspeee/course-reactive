package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import com.example.stream.spring.courses.reactive.example.model.UniversityDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UniversityControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static UniversityDto createUniversityDto(String name, String location, LocalDate established, String accreditation, String president,
                                                     Integer studentCount, String website, String contactEmail, String phoneNumber, String motto,
                                                     String colors, String mascot, Double campusArea, Integer numFaculties, Integer numPrograms,
                                                     Boolean international, Integer ranking, LocalDateTime createdAt, LocalDateTime updatedAt,
                                                     String country, String city) {
        return new UniversityDto(name, location, established, accreditation, president, studentCount, website, contactEmail,
                phoneNumber, motto, colors, mascot, campusArea, numFaculties, numPrograms, international, ranking,
                createdAt, updatedAt, country, city);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    public void find_all_universities() {
        webTestClient.get().uri("/university/getAllUniversities")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void get_university_test() {
        long departmentId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/university/getUniversity")
                        .queryParam("deparmentId", departmentId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CourseDto.class)
                .consumeWith(response -> {
                    CourseDto course = response.getResponseBody();
                    assertNotNull(course);
                });
    }

    @Test
    public void add_university_test() {
        UniversityDto newUniversity = createUniversityDto("Springfield University", "742 Evergreen Terrace, Springfield",
                LocalDate.of(1950, 9, 15), "Higher Learning Commission", "Dr. Jane Smith",
                15000, "https://www.springfielduniversity.edu", "info@springfielduniversity.edu",
                "+1-555-123-4567", "Knowledge and Wisdom", "Blue and Gold", "The Fighting Squirrel",
                150.75, 10, 85, true, 120, LocalDateTime.now(),
                LocalDateTime.now(), "USA", "Springfield");
        // Set other properties as needed

        webTestClient.post().uri("/university/addUniversity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUniversity)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UniversityDto.class)
                .consumeWith(response -> {
                    UniversityDto createdUniversity = response.getResponseBody();
                    assertNotNull(createdUniversity);
                    assertNotNull(createdUniversity.accreditation());
                    assertEquals("Springfield University", createdUniversity.name());
                });
    }

    @Test
    public void delete_university_test() {
        long universityId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/university/deleteUniversity")
                        .queryParam("universityId", universityId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_university_test() {
        UniversityDto existingUniversity = createUniversityDto("Guantanamo University", "742 Evergreen Terrace, Springfield",
                LocalDate.of(1950, 9, 15), "Higher Learning Commission", "Dr. Jane Smith",
                15000, "https://www.springfielduniversity.edu", "info@springfielduniversity.edu",
                "+1-555-123-4567", "Knowledge and Wisdom", "Blue and Gold", "The Fighting Squirrel",
                150.75, 10, 85, true, 120, LocalDateTime.now(),
                LocalDateTime.now(), "USA", "Springfield");

        // Set other properties as needed

        webTestClient.put().uri("/university/updateDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingUniversity)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UniversityDto.class)
                .consumeWith(response -> {
                    UniversityDto updateUniversity = response.getResponseBody();
                    assertNotNull(updateUniversity);
                    assertEquals("Guantanamo University", updateUniversity.name());
                });
    }
}
