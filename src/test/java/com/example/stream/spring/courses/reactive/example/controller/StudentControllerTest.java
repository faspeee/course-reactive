package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.StudentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static StudentRequestDto createStudentDto(String firstName, String lastName, String email, String freshman) {
        return new StudentRequestDto(firstName, lastName, freshman, email);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    void find_all_student_test() {
        webTestClient.get().uri("/student/getAllStudents")
                .exchange().expectStatus().isOk();
    }

    @Test
    void get_student_test() {
        String studentId = "1eb7de5d-746c-493b-865e-b524d7e65d49"; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/student/getStudent")
                        .queryParam("studentId", studentId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StudentResponseDto.class)
                .consumeWith(response -> {
                    StudentResponseDto course = response.getResponseBody();
                    assertNotNull(course);
                });
    }

    @Test
    void add_student_test() {
        StudentRequestDto newStudent = createStudentDto("Fabian", "Aspee Encina", "faspeeencina@gmail.com", "Yes");
        // Set other properties as needed

        webTestClient.post().uri("/student/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newStudent)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentResponseDto.class)
                .consumeWith(response -> {
                    StudentResponseDto studentDTO = response.getResponseBody();
                    assertNotNull(studentDTO);
                    assertNotNull(studentDTO.name());
                    assertEquals("faspeeencina@gmail.com", studentDTO.email());
                });
    }

    @Test
    void delete_student_test() {
        String studentId = "2853e3c3-df5f-4e35-86ff-88b749183b7a"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/student/deleteStudent")
                        .queryParam("studentId", studentId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void update_student_test() {
        StudentRequestDto newStudent = createStudentDto("Fabian", "Aspee Encina", "faspeencina@gmail.com", "Yes");
        // Set other properties as needed

        String studentId = webTestClient.post().uri("/student/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newStudent)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentResponseDto.class)
                .consumeWith(response -> {
                    StudentResponseDto studentDTO = response.getResponseBody();
                    assertNotNull(studentDTO);
                    assertNotNull(studentDTO.name());
                    assertEquals("faspeencina@gmail.com", studentDTO.email());
                }).returnResult().getResponseBody().studentId();

        StudentRequestDto existingStudent = createStudentDto("John", "Dore", "john.doe@student.edu", "No");

        // Set other properties as needed

        webTestClient.put().uri("/student/updateStudent?studentId=" + studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingStudent)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StudentResponseDto.class)
                .consumeWith(response -> {
                    StudentResponseDto updateStudent = response.getResponseBody();
                    assertNotNull(updateStudent);
                    assertEquals("john.doe@student.edu", updateStudent.email());
                });
    }

}
