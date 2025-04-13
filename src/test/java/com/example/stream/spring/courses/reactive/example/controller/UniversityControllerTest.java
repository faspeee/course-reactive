package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.UniversityRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.UniversityResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UniversityControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static UniversityRequestDto createUniversityDto(String name, String location, LocalDate established, String accreditation, String president,
                                                            Integer studentCount, String website, String contactEmail, String phoneNumber, String motto,
                                                            String colors, String mascot, Double campusArea, Integer numFaculties, Integer numPrograms,
                                                            Boolean international, Integer ranking, String identifier,
                                                            String country, String city) {
        return new UniversityRequestDto(name, location, established, accreditation, president, studentCount, website, contactEmail,
                phoneNumber, motto, colors, mascot, campusArea, numFaculties, numPrograms, international, ranking, country, city, identifier);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    void find_all_universities() {
        webTestClient.get().uri("/university/getAllUniversities")
                .exchange().expectStatus().isOk();
    }

    @Test
    void get_university_test() {
        long departmentId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/university/getUniversity")
                        .queryParam("deparmentId", departmentId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UniversityResponseDto.class)
                .consumeWith(response -> {
                    UniversityResponseDto course = response.getResponseBody();
                    assertNotNull(course);
                });
    }

    @Test
    void add_university_test() {
        UniversityRequestDto newUniversity = createUniversityDto("Springfield UniversityService", "742 Evergreen Terrace, Springfield",
                LocalDate.of(1950, 9, 15), "Higher Learning Commission", "Dr. Jane Smith",
                15000, "https://www.springfielduniversity.edu", "info@springfielduniversity.edu",
                "+1-555-123-4567", "Knowledge and Wisdom", "Blue and Gold", "The Fighting Squirrel",
                150.75, 10, 85, true, 120, "id", "USA", "Springfield");
        // Set other properties as needed

        webTestClient.post().uri("/university/addUniversity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUniversity)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UniversityResponseDto.class)
                .consumeWith(response -> {
                    UniversityResponseDto createdUniversity = response.getResponseBody();
                    assertNotNull(createdUniversity);
                    assertNotNull(createdUniversity.accreditation());
                    assertEquals("Springfield UniversityService", createdUniversity.name());
                });
    }

    @DisplayName("add a university on city that not exist return error message")
    @Test
    void add_university_error_test() {
        UniversityRequestDto newUniversity = createUniversityDto("Springfield UniversityService", "742 Evergreen Terrace, Springfield",
                LocalDate.of(1950, 9, 15), "Higher Learning Commission 2", "Dr. Jane Smith",
                15000, "https://www.springfielduniversity.edu", "info@springfielduniversity.edu",
                "+1-555-123-4567", "Knowledge and Wisdom", "Blue and Gold", "The Fighting Squirrel",
                150.75, 10, 85, true, 120, "id 2", "ILLY", "AE");
        // Set other properties as needed

        webTestClient.post().uri("/university/addUniversity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUniversity)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @DisplayName("add a university on country that not exist return error message")
    @Test
    void add_university_error_2_test() {
        UniversityRequestDto newUniversity = createUniversityDto("Springfield UniversityService", "742 Evergreen Terrace, Springfield",
                LocalDate.of(1950, 9, 15), "Higher Learning Commission 2", "Dr. Jane Smith",
                15000, "https://www.springfielduniversity.edu", "info@springfielduniversity.edu",
                "+1-555-123-4567", "Knowledge and Wisdom", "Blue and Gold", "The Fighting Squirrel",
                150.75, 10, 85, true, 120, "id 3", "USA", "AE");
        // Set other properties as needed

        webTestClient.post().uri("/university/addUniversity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUniversity)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void delete_university_test() {
        long universityId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/university/deleteUniversity")
                        .queryParam("universityId", universityId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_university_test() {
        UniversityRequestDto existingUniversity = createUniversityDto("Guantanamo UniversityService", "742 Evergreen Terrace, Springfield",
                LocalDate.of(1950, 9, 15), "Higher Learning Commission", "Dr. Jane Smith",
                15000, "https://www.springfielduniversity.edu", "info@springfielduniversity.edu",
                "+1-555-123-4567", "Knowledge and Wisdom", "Blue and Gold", "The Fighting Squirrel",
                150.75, 10, 85, true, 120, "id 4", "USA", "Springfield");

        // Set other properties as needed

        webTestClient.put().uri("/university/updateDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingUniversity)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UniversityResponseDto.class)
                .consumeWith(response -> {
                    UniversityResponseDto updateUniversity = response.getResponseBody();
                    assertNotNull(updateUniversity);
                    assertEquals("Guantanamo UniversityService", updateUniversity.name());
                });
    }
}
