package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import com.example.stream.spring.courses.reactive.example.model.DepartmentDto;
import com.example.stream.spring.courses.reactive.example.model.StudentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static DepartmentDto createDepartmentDto(String name, String description, String identifier) {
        return new DepartmentDto(name, description, identifier);
    }

    @Test
    public void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    public void find_all_departments() {
        webTestClient.get().uri("/department/getAllDepartment")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void get_department_test() {
        long departmentId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/department/getDepartment")
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
    public void add_department_test() {
        DepartmentDto newDepartment = createDepartmentDto("John Doe", "code course", "282018392KS");
        // Set other properties as needed

        webTestClient.post().uri("/department/addDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newDepartment)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DepartmentDto.class)
                .consumeWith(response -> {
                    DepartmentDto createdDepartment = response.getResponseBody();
                    assertNotNull(createdDepartment);
                    assertNotNull(createdDepartment.identifier());
                    assertEquals("New Course", createdDepartment.name());
                });
    }

    @Test
    public void delete_department_test() {
        long departmentId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/department/deleteDepartment")
                        .queryParam("departmentId", departmentId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_department_test() {
        DepartmentDto existingDepartment = createDepartmentDto("Updated Course Name", "code course", "282018392KS");

        // Set other properties as needed

        webTestClient.put().uri("/department/updateDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingDepartment)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DepartmentDto.class)
                .consumeWith(response -> {
                    DepartmentDto updateDepartment = response.getResponseBody();
                    assertNotNull(updateDepartment);
                    assertEquals("Updated Department Name", updateDepartment.name());
                });
    }

    @Test
    public void get_course_by_department_test() {
        long departmentId = 1; // Replace with a valid course ID
        webTestClient.get().uri("/department/{departmentId}/course", departmentId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StudentDto.class)
                .hasSize(5); // Adjust based on expected data
    }

}
