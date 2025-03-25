package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.StudentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Unit tests for the TeacherController.
 */
@WebFluxTest(TeacherController.class)
public class TeacherControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    /**
     * Tests the retrieval of students by teacher ID.
     */
    @Test
    public void testGetStudentsByTeacher() {
        Long teacherId = 1L;
        StudentDTO student1 = new StudentDTO(1L, "Alice", "alice@example.com");
        StudentDTO student2 = new StudentDTO(2L, "Bob", "bob@example.com");

        webTestClient.get()
                .uri("/teachers/{teacherId}/students", teacherId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StudentDTO.class)
                .hasSize(2)
                .contains(student1, student2);
    }
}
