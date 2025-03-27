package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the TeacherController.
 */


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InstructorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

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
        StudentResponseDto student1 = new StudentResponseDto(1L, "Alice", "Nel paese", "alice@example.com");
        StudentResponseDto student2 = new StudentResponseDto(2L, "Bob", "Patigno", "bob@example.com");

        webTestClient.get()
                .uri("/teachers/{teacherId}/students", teacherId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StudentResponseDto.class)
                .hasSize(2)
                .contains(student1, student2);
    }
}
