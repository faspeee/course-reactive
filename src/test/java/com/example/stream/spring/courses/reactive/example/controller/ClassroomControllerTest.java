package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.ClassroomDto;
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
public class ClassroomControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static ClassroomDto createClassroomDto(Long buildingId, String roomNumber, Integer capacity, LocalDateTime createdAt,
                                                   LocalDateTime updatedAt) {
        return new ClassroomDto(buildingId, roomNumber, capacity, createdAt, updatedAt);
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
        ClassroomDto newClassroom = createClassroomDto(1L, "XVI", 10, LocalDateTime.now(), null);
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
                    assertNotNull(createdClassroom.capacity());
                    assertEquals(10, createdClassroom.capacity());
                });
    }

    @DisplayName("add classroom on building that not exist return error")
    @Test
    public void add_classroom_error_test() {
        ClassroomDto newClassroom = createClassroomDto(11111L, "XVI", 10, LocalDateTime.now(), null);
        // Set other properties as needed

        webTestClient.post().uri("/classroom/addDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newClassroom)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void delete_classroom_test() {
        long classroomId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/classroom/deleteDepartment")
                        .queryParam("classroomId", classroomId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_classroom_test() {
        ClassroomDto existingClassroom = createClassroomDto(1L, "XVIII", 10, LocalDateTime.now(), null);

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
                    assertEquals("XVIII", updateCollege.roomNumber());
                });
    }

}
