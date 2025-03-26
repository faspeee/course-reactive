package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.ClassroomDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClassroomControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static ClassroomDto createClassroomDto(String updatedCourseName, String codeCourse, String s) {
        return new ClassroomDto();
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    public void find_all_departments() {
        webTestClient.get().uri("/classroom/getAllDepartment")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void get_classroom_test() {
        long departmentId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/classroom/getDepartment")
                        .queryParam("deparmentId", departmentId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClassroomDto.class)
                .consumeWith(response -> {
                    ClassroomDto classroomDto = response.getResponseBody();
                    assertNotNull(classroomDto);
                });
    }

    @Test
    public void add_classroom_test() {
        ClassroomDto newClassroom = createClassroomDto("John Doe", "code course", "282018392KS");
        // Set other properties as needed

        webTestClient.post().uri("/classroom/addDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newClassroom)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClassroomDto.class)
                .consumeWith(response -> {
                    ClassroomDto createdClassroom = response.getResponseBody();
                    assertNotNull(createdClassroom);
                    assertNotNull(createdClassroom.identifier());
                    assertEquals("New Course", createdClassroom.name());
                });
    }

    @Test
    public void delete_classroom_test() {
        long classroomId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/classroom/deleteDepartment/{id}")
                        .queryParam("classroomId", classroomId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_classroom_test() {
        ClassroomDto existingClassroom = createClassroomDto("Updated Course Name", "code course", "282018392KS");

        // Set other properties as needed

        webTestClient.put().uri("/classroom/updateDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingClassroom)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClassroomDto.class)
                .consumeWith(response -> {
                    ClassroomDto updateCollege = response.getResponseBody();
                    assertNotNull(updateCollege);
                    assertEquals("Updated Department Name", updateCollege.name());
                });
    }

}
