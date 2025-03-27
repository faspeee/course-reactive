package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CollegeRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CollegeResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CollegeControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static CollegeRequestDto createCollegeDto(String name, String dean, Long universityId, String identifier) {
        return new CollegeRequestDto(name, dean, universityId, identifier);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    void find_all_college() {
        webTestClient.get().uri("/college/getAllCollege")
                .exchange().expectStatus().isOk();
    }

    @Test
    void get_college_test() {
        long collegeId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/college/getCollege")
                        .queryParam("collegeId", collegeId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CollegeResponseDto.class)
                .consumeWith(response -> {
                    CollegeResponseDto college = response.getResponseBody();
                    assertNotNull(college);
                });
    }

    @Test
    void add_college_test() {
        CollegeRequestDto newCollege = createCollegeDto("John Doe", "that is it", 1L, "id 3");
        // Set other properties as needed

        webTestClient.post().uri("/college/addCollege")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCollege)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CollegeResponseDto.class)
                .consumeWith(response -> {
                    CollegeResponseDto createdCollege = response.getResponseBody();
                    assertNotNull(createdCollege);
                    assertNotNull(createdCollege.universityId());
                    assertEquals("John Doe", createdCollege.name());
                });
    }

    @DisplayName("add college in university that not exist, return error")
    @Test
    void add_college_error_test() {
        CollegeRequestDto newCollege = createCollegeDto("John Doe", "that is it", 2231L, "id 2");
        // Set other properties as needed

        webTestClient.post().uri("/college/addCollege")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCollege)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void delete_college_test() {
        long collegeId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/college/deleteCollege")
                        .queryParam("collegeId", collegeId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_college_test() {
        CollegeRequestDto existingCollege = createCollegeDto("John week", "that is it", 1L, "id");

        // Set other properties as needed

        webTestClient.put().uri("/college/updateCollege")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingCollege)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CollegeResponseDto.class)
                .consumeWith(response -> {
                    CollegeResponseDto updateCollege = response.getResponseBody();
                    assertNotNull(updateCollege);
                    assertEquals("John week", updateCollege.name());
                });
    }
}
