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

    private static ClassroomRequestDto createClassroomDto(String buildingId, String roomNumber, Integer capacity, String identifier) {
        return new ClassroomRequestDto(buildingId, roomNumber, capacity, identifier);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    void find_all_classroom() {
        webTestClient.get().uri("/classroom/getAllClassroom")
                .exchange().expectStatus().isOk();
    }

    @Test
    void get_classroom_test() {
        String classroomId = "76aa28de-baa8-4a90-9a7c-46dfa31da5b8"; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/classroom/getClassroom")
                        .queryParam("classroomId", classroomId)
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
        ClassroomRequestDto newClassroom = createClassroomDto("3ceaff23-8f6e-4557-9fc6-c294f64063d4", "XVI", 10, "id");
        // Set other properties as needed

        webTestClient.post().uri("/classroom/addClassroom")
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
        ClassroomRequestDto newClassroom = createClassroomDto("3ceaff23-8f6e-4557-9fc6-c294f64063d2", "XVI", 10, "id2");
        // Set other properties as needed

        webTestClient.post().uri("/classroom/addClassroom")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newClassroom)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @DisplayName("add classroom with erroneous UUID buildingId")
    @Test
    void add_classroom_internal_error_test() {
        ClassroomRequestDto newClassroom = createClassroomDto("111111L", "XVI", 10, "id2");
        // Set other properties as needed

        webTestClient.post().uri("/classroom/addClassroom")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newClassroom)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void delete_classroom_test() {
        String classroomId = "ddfb7d3e-c7c8-4495-a1b2-8aa6736f09ca"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/classroom/deleteClassroom")
                        .queryParam("classroomId", classroomId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_classroom_test() {

        ClassroomRequestDto newClassroom = createClassroomDto("3ceaff23-8f6e-4557-9fc6-c294f64063d4", "XVI", 10, "id");
        // Set other properties as needed

        String classroomId = webTestClient.post().uri("/classroom/addClassroom")
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
                }).returnResult().getResponseBody().classroomId();

        ClassroomRequestDto existingClassroom = createClassroomDto("3ceaff23-8f6e-4557-9fc6-c294f64063d4", "XVIII", 10, "id3");

        // Set other properties as needed

        webTestClient.put().uri("/classroom/updateClassroom?classroomId=" + classroomId)
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
