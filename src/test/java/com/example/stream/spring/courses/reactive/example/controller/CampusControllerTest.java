package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CampusRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CampusResponseDto;
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
class CampusControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static CampusRequestDto createCampusDto(String name, String address, String universityId,
                                                    LocalDateTime startDate, LocalDateTime endDate,
                                                    String identifier, String country, String city) {
        return new CampusRequestDto(name, address, universityId, country, city, startDate, endDate, identifier);
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
        String campusId = "e4b44ebc-7369-4c79-96d4-4e0c61034efc"; // Replace with a valid course ID
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
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", "068ec73a-5210-4891-b4d2-a988541e3854",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3), "identifier", "Italy", "Napoli");
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

    @DisplayName("when try to add campus with erroneous uuid , the service throw error")
    @Test
    void add_campus_error_uuid_test() {
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", "1L",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3),
                "identifier", "Italy", "Napoli");
        // Set other properties as needed

        webTestClient.post().uri("/campus/addCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCampusDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @DisplayName("when try to add campus on not found university , the service throw error")
    @Test
    void add_campus_error_university_test() {
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", "068ec73a-5210-4891-b4d2-a988541e3851",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3),
                "identifier", "Italy", "Napoli");
        // Set other properties as needed

        webTestClient.post().uri("/campus/addCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCampusDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("The university is not found");
        ;
    }

    @DisplayName("add campus on country that not exist return error")
    @Test
    void add_campus_error_test() {
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", "1L",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3),
                "identifier 2", "Lavazza", "Napoli");
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
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", "1L",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3),
                "identifier 2", "Spain", "Napoli");
        // Set other properties as needed

        webTestClient.post().uri("/campus/addCampus")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCampusDto)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void delete_campus_test() {
        String campusId = "5919b48f-1220-46df-b966-52143ef2f995"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/campus/deleteCampus")
                        .queryParam("campusId", campusId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_campus_test() {
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", "068ec73a-5210-4891-b4d2-a988541e3854",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3), "identifier", "Italy", "Napoli");
        // Set other properties as needed

        String campusId = webTestClient.post().uri("/campus/addCampus")
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
                }).returnResult().getResponseBody().campusId();

        CampusRequestDto existingCampus = createCampusDto("Campus Messi", "Napoli 550", "068ec73a-5210-4891-b4d2-a988541e3854",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3),
                "identifier 2", "Italy", "Napoli");

        // Set other properties as needed

        webTestClient.put().uri("/campus/updateCampus?campusId=" + campusId)
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

    @Test
    void update_campus_not_found_test() {


        CampusRequestDto existingCampus = createCampusDto("Campus Messi", "Napoli 550", "068ec73a-5210-4891-b4d2-a988541e3854",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3),
                "identifier 2", "Italy", "Napoli");
        // Set other properties as needed
        webTestClient.put().uri("/campus/updateCampus?campusId=" + "068ec73a-5210-4891-b4d2-a988541e3854")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingCampus)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.message").isEqualTo("The campus is not found");
    }

    @Test
    void update_campus_university_not_found_test() {
        CampusRequestDto newCampusDto = createCampusDto("Campus Maradona", "Napoli 500", "068ec73a-5210-4891-b4d2-a988541e3854",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3), "identifier", "Italy", "Napoli");
        // Set other properties as needed

        String campusId = webTestClient.post().uri("/campus/addCampus")
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
                }).returnResult().getResponseBody().campusId();

        CampusRequestDto existingCampus = createCampusDto("Campus Messi", "Napoli 550", "068ec73a-5210-4891-b1d2-a988541e3854",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(3),
                "identifier 2", "Italy", "Napoli");

        // Set other properties as needed

        webTestClient.put().uri("/campus/updateCampus?campusId=" + campusId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingCampus)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.message").isEqualTo("The university is not found");
        ;
    }
}
