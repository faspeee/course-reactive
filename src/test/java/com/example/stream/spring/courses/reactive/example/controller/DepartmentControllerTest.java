package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.DepartmentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.DepartmentResponseDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DepartmentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static DepartmentRequestDto createDepartmentDto(String name, String description, String identifier) {
        return new DepartmentRequestDto(name, description, identifier);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    void find_all_departments() {
        webTestClient.get().uri("/department/getAllDepartment")
                .exchange().expectStatus().isOk();
    }

    @Test
    void get_department_test() {
        long departmentId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/department/getDepartment")
                        .queryParam("deparmentId", departmentId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DepartmentResponseDto.class)
                .consumeWith(response -> {
                    DepartmentResponseDto course = response.getResponseBody();
                    assertNotNull(course);
                });
    }

    @Test
    void add_department_test() {
        DepartmentRequestDto newDepartment = createDepartmentDto("John Doe", "code course", "282018392KS");
        // Set other properties as needed

        webTestClient.post().uri("/department/addDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newDepartment)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DepartmentResponseDto.class)
                .consumeWith(response -> {
                    DepartmentResponseDto createdDepartment = response.getResponseBody();
                    assertNotNull(createdDepartment);
                    assertNotNull(createdDepartment.identifier());
                    assertEquals("New Course", createdDepartment.name());
                });
    }

    @Test
    void delete_department_test() {
        long departmentId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/department/deleteDepartment")
                        .queryParam("departmentId", departmentId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_department_test() {
        DepartmentRequestDto existingDepartment = createDepartmentDto("Updated Course Name", "code course", "282018392KS");

        // Set other properties as needed

        webTestClient.put().uri("/department/updateDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingDepartment)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DepartmentResponseDto.class)
                .consumeWith(response -> {
                    DepartmentResponseDto updateDepartment = response.getResponseBody();
                    assertNotNull(updateDepartment);
                    assertEquals("Updated Department Name", updateDepartment.name());
                });
    }

    @Test
    void get_course_by_department_test() {
        long departmentId = 1; // Replace with a valid course ID
        webTestClient.get().uri("/department/{departmentId}/course", departmentId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StudentResponseDto.class)
                .hasSize(5); // Adjust based on expected data
    }

}
