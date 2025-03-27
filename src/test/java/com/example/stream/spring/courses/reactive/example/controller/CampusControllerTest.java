package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CampusRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CampusResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CampusControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static CampusRequestDto createCampusDto(String name, String address, Long universityId, String identifier, String country, String city) {
        return new CampusRequestDto(name, address, universityId, country, city, identifier);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    void find_all_campus() {
        webTestClient.get().uri("/campus/getAllCampus")
                .exchange().expectStatus().isOk();
    }

    @Test
    void get_campus_test() {
        long campusId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/campus/getCampus")
                        .queryParam("campusId", campusId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CampusResponseDto.class)
                .consumeWith(response -> {
                    CampusResponseDto campus = response.getResponseBody();
                    assertNotNull(campus);
                });
    }

    @Test
    void add_campus_test() {
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", 1L, "identifier", "Italy", "Napoli");
        // Set other properties as needed

        webTestClient.post().uri("/campus/addCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCampusDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CampusResponseDto.class)
                .consumeWith(response -> {
                    CampusResponseDto createCampus = response.getResponseBody();
                    assertNotNull(createCampus);
                    assertNotNull(createCampus.country());
                    assertEquals("Campus Maradona", createCampus.name());
                });
    }

    @DisplayName("add campus on country that not exist return error")
    @Test
    void add_campus_error_test() {
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", 1L, "identifier 2", "Lavazza", "Napoli");
        // Set other properties as needed

        webTestClient.post().uri("/campus/addCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCampusDto)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @DisplayName("add campus on city that not exist on the country return error")
    @Test
    void add_campus_error_2_test() {
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", 1L, "identifier 2", "Spain", "Napoli");
        // Set other properties as needed

        webTestClient.post().uri("/campus/addCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCampusDto)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void delete_campus_test() {
        long campusId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/campus/deleteCampus")
                        .queryParam("campusId", campusId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_campus_test() {
        CampusRequestDto existingCampus = createCampusDto("Campus Messi", "Napoli 550", 1L, "identifier 2", "Italy", "Napoli");

        // Set other properties as needed

        webTestClient.put().uri("/campus/updateCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingCampus)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CampusResponseDto.class)
                .consumeWith(response -> {
                    CampusResponseDto updatedCampus = response.getResponseBody();
                    assertNotNull(updatedCampus);
                    assertEquals("Campus Messi", updatedCampus.name());
                });
    }
}
