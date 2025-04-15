package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.BuildingRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.BuildingResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BuildingControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    void find_all_building() {
        webTestClient.get().uri("/building/getAllBuilding")
                .exchange().expectStatus().isOk();
    }

    @Test
    void get_building_test() {
        String buildingId = "3ceaff23-8f6e-4557-9fc6-c294f64063d4"; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/building/getBuildingById")
                        .queryParam("buildingId", buildingId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BuildingResponseDto.class)
                .consumeWith(response -> {
                    BuildingResponseDto course = response.getResponseBody();
                    assertNotNull(course);
                });
    }

    @Test
    void add_building_test() {
        BuildingRequestDto newBuilding = createBuildingDto("Carlos Filling X", "XDS191", "e4b44ebc-7369-4c79-96d4-4e0c61034efc", "identifier");
        // Set other properties as needed

        webTestClient.post().uri("/building/addBuilding")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newBuilding)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BuildingResponseDto.class)
                .consumeWith(response -> {
                    BuildingResponseDto createBuilding = response.getResponseBody();
                    assertNotNull(createBuilding);
                    assertNotNull(createBuilding.createdAt());
                    assertEquals("Carlos Filling X", createBuilding.name());
                });
    }

    @DisplayName("adding building in campus that not exist return error")
    @Test
    void add_building_error_test() {
        BuildingRequestDto newBuilding = createBuildingDto("Carlos Filling X", "ZZT911", "3ceaff23-8f6e-4557-9fc6-c294f64063d4", "identifier");
        // Set other properties as needed
        webTestClient.post().uri("/building/addBuilding")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newBuilding)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void delete_building_test() {
        String buildingId = "4806db08-b4fd-49d9-b099-2b8b79d13259"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/building/deleteBuilding")
                        .queryParam("buildingId", buildingId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_building_test() {
        BuildingRequestDto newBuilding = createBuildingDto("Carlos Filling X", "XDS192", "e4b44ebc-7369-4c79-96d4-4e0c61034efc", "identifier");
        // Set other properties as needed

        String buildingId = webTestClient.post().uri("/building/addBuilding")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newBuilding)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BuildingResponseDto.class)
                .consumeWith(response -> {
                    BuildingResponseDto createBuilding = response.getResponseBody();
                    assertNotNull(createBuilding);
                    assertNotNull(createBuilding.createdAt());
                    assertEquals("Carlos Filling X", createBuilding.name());
                }).returnResult().getResponseBody().buildingId();
        BuildingRequestDto existedBuilding = createBuildingDto("Carlos Filling", "XDS192", "e4b44ebc-7369-4c79-96d4-4e0c61034efc", "identifier");
        
        // Set other properties as needed
        webTestClient.put().uri(uriBuilder -> uriBuilder.path("/building/updateBuilding")
                        .queryParam("buildingId", buildingId)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existedBuilding)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BuildingResponseDto.class)
                .consumeWith(response -> {
                    BuildingResponseDto updateBuilding = response.getResponseBody();
                    assertNotNull(updateBuilding);
                    assertEquals("Carlos Filling", updateBuilding.name());
                });
    }

    private BuildingRequestDto createBuildingDto(String name, String code, String campusId, String identifier) {
        return new BuildingRequestDto(name, code, campusId, identifier);
    }

}
