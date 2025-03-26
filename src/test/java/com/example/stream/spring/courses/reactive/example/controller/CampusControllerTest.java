package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CampusDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampusControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static CampusDto createCampusDto(String name, String address, Long universityId, LocalDateTime createdAt,
                                             LocalDateTime updatedAt, String country, String city) {
        return new CampusDto(name, address, universityId, createdAt, updatedAt, country, city);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    public void find_all_campus() {
        webTestClient.get().uri("/campus/getAllCampus")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void get_campus_test() {
        long campusId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/campus/getCampus")
                        .queryParam("campusId", campusId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CampusDto.class)
                .consumeWith(response -> {
                    CampusDto campus = response.getResponseBody();
                    assertNotNull(campus);
                });
    }

    @Test
    public void add_campus_test() {
        CampusDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", 1L, LocalDateTime.now(), null, "Italy", "Napoli");
        // Set other properties as needed

        webTestClient.post().uri("/campus/addCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCampusDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CampusDto.class)
                .consumeWith(response -> {
                    CampusDto createCampus = response.getResponseBody();
                    assertNotNull(createCampus);
                    assertNotNull(createCampus.country());
                    assertEquals("Campus Maradona", createCampus.name());
                });
    }

    @DisplayName("add campus on country that not exist return error")
    @Test
    public void add_campus_error_test() {
        CampusDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", 1L, LocalDateTime.now(), null, "Lavazza", "Napoli");
        // Set other properties as needed

        webTestClient.post().uri("/campus/addCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCampusDto)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @DisplayName("add campus on city that not exist on the country return error")
    @Test
    public void add_campus_error_2_test() {
        CampusDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", 1L, LocalDateTime.now(), null, "Spain", "Napoli");
        // Set other properties as needed

        webTestClient.post().uri("/campus/addCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCampusDto)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void delete_campus_test() {
        long campusId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/campus/deleteCampus")
                        .queryParam("campusId", campusId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_campus_test() {
        CampusDto existingCampus = createCampusDto("Campus Messi", "Napoli 550", 1L, null, LocalDateTime.now(), "Italy", "Napoli");

        // Set other properties as needed

        webTestClient.put().uri("/campus/updateCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingCampus)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CampusDto.class)
                .consumeWith(response -> {
                    CampusDto updatedCampus = response.getResponseBody();
                    assertNotNull(updatedCampus);
                    assertEquals("Campus Messi", updatedCampus.name());
                });
    }
}
