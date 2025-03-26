package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CollegeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CollegeControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static CollegeDto createCollegeDto(String updatedCourseName, String codeCourse, String s) {
        return new CollegeDto();
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    public void find_all_college() {
        webTestClient.get().uri("/college/getAllCollege")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void get_college_test() {
        long collegeId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/college/getCollege")
                        .queryParam("collegeId", collegeId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CollegeDto.class)
                .consumeWith(response -> {
                    CollegeDto college = response.getResponseBody();
                    assertNotNull(college);
                });
    }

    @Test
    public void add_college_test() {
        CollegeDto newCollege = createCollegeDto("John Doe", "code course", "282018392KS");
        // Set other properties as needed

        webTestClient.post().uri("/college/addCollege")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCollege)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CollegeDto.class)
                .consumeWith(response -> {
                    CollegeDto createdCollege = response.getResponseBody();
                    assertNotNull(createdCollege);
                    assertNotNull(createdCollege.identifier());
                    assertEquals("New Course", createdCollege.name());
                });
    }

    @Test
    public void delete_college_test() {
        long collegeId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/college/deleteCollege/{id}")
                        .queryParam("collegeId", collegeId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_college_test() {
        CollegeDto existingCollege = createCollegeDto("Updated Course Name", "code course", "282018392KS");

        // Set other properties as needed

        webTestClient.put().uri("/college/updateCollege")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingCollege)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CollegeDto.class)
                .consumeWith(response -> {
                    CollegeDto updateCollege = response.getResponseBody();
                    assertNotNull(updateCollege);
                    assertEquals("Updated Department Name", updateCollege.name());
                });
    }
}
