package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import com.example.stream.spring.courses.reactive.example.model.StudentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static StudentDto createStudentDto(String firstName, String lastName, String email) {
        return new StudentDto(firstName, lastName, email);
    }

    @Test
    public void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    public void find_all_student_test() {
        webTestClient.get().uri("/student/getAllStudents")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void get_student_test() {
        long studentId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/student/getStudent")
                        .queryParam("studentId", studentId)
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
    public void add_student_test() {
        StudentDto newStudent = createStudentDto("Fabian", "Aspee Encina", "faspeeencina@gmail.com");
        // Set other properties as needed

        webTestClient.post().uri("/student/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newStudent)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDto.class)
                .consumeWith(response -> {
                    StudentDto studentDTO = response.getResponseBody();
                    assertNotNull(studentDTO);
                    assertNotNull(studentDTO.name());
                    assertEquals("New Student", studentDTO.email());
                });
    }

    @Test
    public void delete_student_test() {
        long studentId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/student/deleteStudent")
                        .queryParam("studentId", studentId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void update_student_test() {
        StudentDto existingStudent = createStudentDto("John", "Dore", "john.doe@student.edu");

        // Set other properties as needed

        webTestClient.put().uri("/student/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingStudent)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StudentDto.class)
                .consumeWith(response -> {
                    StudentDto updateStudent = response.getResponseBody();
                    assertNotNull(updateStudent);
                    assertEquals("Updated Student Name", updateStudent.name());
                });
    }

}
