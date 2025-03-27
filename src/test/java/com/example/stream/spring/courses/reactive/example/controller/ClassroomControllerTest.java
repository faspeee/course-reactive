package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.ClassroomRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.ClassroomResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClassroomControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static ClassroomRequestDto createClassroomDto(Long buildingId, String roomNumber, Integer capacity, String identifier) {
        return new ClassroomRequestDto(buildingId, roomNumber, capacity, identifier);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    void find_all_departments() {
        webTestClient.get().uri("/classroom/getAllDepartment")
                .exchange().expectStatus().isOk();
    }

    @Test
    void get_classroom_test() {
        long departmentId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/classroom/getDepartment")
                        .queryParam("deparmentId", departmentId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClassroomResponseDto.class)
                .consumeWith(response -> {
                    ClassroomResponseDto classroomDto = response.getResponseBody();
                    assertNotNull(classroomDto);
                });
    }

    @Test
    void add_classroom_test() {
        ClassroomRequestDto newClassroom = createClassroomDto(1L, "XVI", 10, "id");
        // Set other properties as needed

        webTestClient.post().uri("/classroom/addDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newClassroom)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClassroomResponseDto.class)
                .consumeWith(response -> {
                    ClassroomResponseDto createdClassroom = response.getResponseBody();
                    assertNotNull(createdClassroom);
                    assertNotNull(createdClassroom.capacity());
                    assertEquals(10, createdClassroom.capacity());
                });
    }

    @DisplayName("add classroom on building that not exist return error")
    @Test
    void add_classroom_error_test() {
        ClassroomRequestDto newClassroom = createClassroomDto(11111L, "XVI", 10, "id2");
        // Set other properties as needed

        webTestClient.post().uri("/classroom/addDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newClassroom)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void delete_classroom_test() {
        long classroomId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/classroom/deleteDepartment")
                        .queryParam("classroomId", classroomId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_classroom_test() {
        ClassroomRequestDto existingClassroom = createClassroomDto(1L, "XVIII", 10, "id3");

        // Set other properties as needed

        webTestClient.put().uri("/classroom/updateDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingClassroom)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClassroomResponseDto.class)
                .consumeWith(response -> {
                    ClassroomResponseDto updateCollege = response.getResponseBody();
                    assertNotNull(updateCollege);
                    assertEquals("XVIII", updateCollege.roomNumber());
                });
    }

}
