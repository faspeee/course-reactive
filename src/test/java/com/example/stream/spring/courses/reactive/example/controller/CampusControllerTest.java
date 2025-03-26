package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CampusDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CampusControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static CampusDto createCampusDto(String updatedCourseName, String codeCourse, String s) {
        return new CampusDto();
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
        CampusDto newCampusDto = createCampusDto("John Doe", "code course", "282018392KS");
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
                    assertNotNull(createCampus.identifier());
                    assertEquals("New Course", createCampus.name());
                });
    }

    @Test
    public void delete_campus_test() {
        long departmentId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/campus/deleteCampus/{id}")
                        .queryParam("courseId", departmentId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_campus_test() {
        CampusDto existingCampus = createCampusDto("Updated Course Name", "code course", "282018392KS");

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
                    assertEquals("Updated Department Name", updatedCampus.name());
                });
    }
}
