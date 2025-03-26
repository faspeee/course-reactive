package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.BuildingDto;
import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BuildingControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    public void find_all_building() {
        webTestClient.get().uri("/building/getAllBuilding")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void get_building_test() {
        long buildingId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/building/getBuildingById")
                        .queryParam("buildingId", buildingId)
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
    public void add_building_test() {
        BuildingDto newBuilding = createBuildingDto("John Doe", "code course", "282018392KS");
        // Set other properties as needed

        webTestClient.post().uri("/building/addBuilding")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newBuilding)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BuildingDto.class)
                .consumeWith(response -> {
                    BuildingDto createdDepartment = response.getResponseBody();
                    assertNotNull(createdDepartment);
                    assertNotNull(createdDepartment.identifier());
                    assertEquals("New Course", createdDepartment.name());
                });
    }

    @Test
    public void delete_building_test() {
        long buildingId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/building/deleteBuilding/{id}")
                        .queryParam("courseId", buildingId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_building_test() {
        BuildingDto existedBuilding = createBuildingDto("Updated Course Name", "code course", "282018392KS");

        // Set other properties as needed

        webTestClient.put().uri("/building/updateBuilding")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existedBuilding)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BuildingDto.class)
                .consumeWith(response -> {
                    BuildingDto updateDepartment = response.getResponseBody();
                    assertNotNull(updateDepartment);
                    assertEquals("Updated Department Name", updateDepartment.name());
                });
    }

    private BuildingDto createBuildingDto(String updatedCourseName, String codeCourse, String s) {
        return new BuildingDto();
    }

}
