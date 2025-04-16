package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.InstructorRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.InstructorResponseDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the TeacherController.
 */


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InstructorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private static InstructorRequestDto createInstructorRequestDto(String firstName, String email, String identifier) {
        return new InstructorRequestDto(firstName, email, identifier);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    /**
     * Tests the retrieval of students by teacher ID.
     */
    @Test
    void testGetStudentsByTeacher() {
        Long teacherId = 1L;
        StudentResponseDto student1 = new StudentResponseDto("1L", "Alice", "Nel paese", "alice@example.com");
        StudentResponseDto student2 = new StudentResponseDto("2L", "Bob", "Patigno", "bob@example.com");

        webTestClient.get()
                .uri("/teacher/{teacherId}/students", teacherId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StudentResponseDto.class)
                .hasSize(2)
                .contains(student1, student2);
    }

    @Test
    void find_all_instructor() {
        webTestClient.get().uri("/teacher/getAllInstructors")
                .exchange().expectStatus().isOk();
    }

    @Test
    void get_instructor_by_id_test() {
        String instructorId = "520ee975-1224-42e6-8255-a9712e2bc22c"; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/teacher/getInstructorById")
                        .queryParam("instructorId", instructorId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(InstructorResponseDto.class)
                .consumeWith(response -> {
                    InstructorResponseDto instructorResponse = response.getResponseBody();
                    assertNotNull(instructorResponse);
                });
    }

    @Test
    void add_instructor_test() {
        InstructorRequestDto newInstructor = createInstructorRequestDto("John Doe", "yourinstructor@email.com", "282018392KS");
        // Set other properties as needed

        webTestClient.post().uri("/teacher/addInstructor")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newInstructor)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(InstructorResponseDto.class)
                .consumeWith(response -> {
                    InstructorResponseDto instructor = response.getResponseBody();
                    assertNotNull(instructor);
                    assertNotNull(instructor.email());
                    assertEquals("John Doe", instructor.name());
                });
    }

    @Test
    void add_instructor_error_email_test() {
        InstructorRequestDto newInstructor = createInstructorRequestDto("John Doe", "code course", "282018392KS");
        // Set other properties as needed

        webTestClient.post().uri("/teacher/addInstructor")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newInstructor)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void delete_teacher_test() {
        String instructorId = "705ac9e9-f37a-4d13-9a93-e933346c2e7f"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/teacher/deleteInstructor")
                        .queryParam("instructorId", instructorId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_teacher_not_found_test() {
        String instructorId = "705ac9e9-f37a-4d13-9a93-1933346c2e7f"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/teacher/deleteInstructor")
                        .queryParam("instructorId", instructorId)
                        .build())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void delete_teacher_internal_error_test() {
        String instructorId = "11111"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/teacher/deleteInstructor")
                        .queryParam("instructorId", instructorId)
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void update_instructor_test() {
        InstructorRequestDto newInstructor = createInstructorRequestDto("John Doe", "yourinstructor2@email.com", "282018392KS");
        // Set other properties as needed

        String instructorId = webTestClient.post().uri("/teacher/addInstructor")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newInstructor)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(InstructorResponseDto.class)
                .consumeWith(response -> {
                    InstructorResponseDto instructor = response.getResponseBody();
                    assertNotNull(instructor);
                    assertNotNull(instructor.email());
                    assertEquals("John Doe", instructor.name());
                }).returnResult().getResponseBody().instructorId();

        InstructorRequestDto existingInstructor = createInstructorRequestDto("Updated instructor Name", "instructor@email.com", "282018392KS");

        // Set other properties as needed

        webTestClient.put().uri("/teacher/updateInstructor?instructorId=" + instructorId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingInstructor)
                .exchange()
                .expectStatus().isOk()
                .expectBody(InstructorResponseDto.class)
                .consumeWith(response -> {
                    InstructorResponseDto instructorResponseDto = response.getResponseBody();
                    assertNotNull(instructorResponseDto);
                    assertEquals("Updated instructor Name", instructorResponseDto.name());
                });
    }

}
