package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.StudentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the TeacherController.
 */


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InstructorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void dummyTest() {
        assertNotNull(webTestClient);
    }

    /**
     * Tests the retrieval of students by teacher ID.
     */
    @Test
    public void testGetStudentsByTeacher() {
        Long teacherId = 1L;
        StudentDto student1 = new StudentDto(1L, "Alice", "Nel paese", "alice@example.com");
        StudentDto student2 = new StudentDto(2L, "Bob", "Patigno", "bob@example.com");

        webTestClient.get()
                .uri("/teachers/{teacherId}/students", teacherId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StudentDto.class)
                .hasSize(2)
                .contains(student1, student2);
    }
}
