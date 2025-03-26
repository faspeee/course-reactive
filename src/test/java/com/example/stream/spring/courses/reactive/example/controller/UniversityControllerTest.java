package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import com.example.stream.spring.courses.reactive.example.model.UniversityDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UniversityControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static UniversityDto crateUniversityDto(String updatedCourseName, String codeCourse, String s) {
        return new UniversityDto();
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
        UniversityDto newUniversity = crateUniversityDto("John Doe", "code course", "282018392KS");
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
                    assertNotNull(createdUniversity.identifier());
                    assertEquals("New Course", createdUniversity.name());
                });
    }

    @Test
    public void delete_department_test() {
        long universityId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/university/deleteUniversity/{id}")
                        .queryParam("universityId", universityId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_university_test() {
        UniversityDto existingUniversity = crateUniversityDto("Updated Course Name", "code course", "282018392KS");

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
                    assertEquals("Updated Department Name", updateUniversity.name());
                });
    }
}
