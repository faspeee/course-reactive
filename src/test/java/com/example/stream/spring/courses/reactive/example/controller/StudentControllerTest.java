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
    void get_student_not_found_test() {
        String studentId = "1e17de5d-746c-493b-865e-b524d7e65d49"; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/student/getStudent")
                        .queryParam("studentId", studentId)
                        .build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void get_student_server_error_test() {
        String studentId = "2342"; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/student/getStudent")
                        .queryParam("studentId", studentId)
                        .build())
                .exchange()
                .expectStatus().is5xxServerError();
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
    void delete_student_not_found_test() {
        String studentId = "2853e3c3-d15f-4e35-86ff-88b749183b7a"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/student/deleteStudent")
                        .queryParam("studentId", studentId)
                        .build())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Student not found");
    }

    @Test
    void delete_student_server_error_test() {
        String studentId = "1232"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/student/deleteStudent")
                        .queryParam("studentId", studentId)
                        .build())
                .exchange()
                .expectStatus().is5xxServerError().expectBody()
                .jsonPath("$.message").isEqualTo("Invalid UUID string: 1232");
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

    @Test
    void test_get_students_by_course() {
        String courseId = "7475417a-f970-4366-aa9f-55c7bb66fe0a"; // Replace with a valid course ID
        webTestClient.get().uri("/student/{courseId}/studentsByCourse", courseId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StudentResponseDto.class)
                .hasSize(5); // Adjust based on expected data
    }

    @Test
    void test_get_students_by_course_not_found() {
        String courseId = "7475412a-f970-4366-aa9f-55c7bb66fe0a"; // Replace with a valid course ID
        webTestClient.get().uri("/student/{courseId}/studentsByCourse", courseId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("The course is not found"); // Adjust based on expected data
    }

    @Test
    void test_get_students_by_course_error_server() {
        String courseId = "1232"; // Replace with a valid course ID
        webTestClient.get().uri("/student/{courseId}/studentsByCourse", courseId)
                .exchange()
                .expectStatus().is5xxServerError().expectBody()
                .jsonPath("$.message").isEqualTo("Invalid UUID string: 1232"); // Adjust based on expected data
    }

    /**
     * Tests the retrieval of students by teacher ID.
     */
    @Test
    void test_get_students_by_teacher() {
        String teacherId = "520ee975-1224-42e6-8255-a9712e2bc22c";
        StudentResponseDto student1 = new StudentResponseDto("1eb7de5d-746c-493b-865e-b524d7e65d49", "Alice", "Williams", "alice.williams@student.globaltech.edu");
        webTestClient.get()
                .uri("/student/{teacherId}/studentsByTeacher", teacherId)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StudentResponseDto.class)
                .hasSize(1)
                .contains(student1);
    }

    @Test
    void test_get_students_by_teacher_not_found() {
        String teacherId = "520ee975-1224-41e6-8255-a9712e2bc22c";
        webTestClient.get()
                .uri("/student/{teacherId}/studentsByTeacher", teacherId)
                .exchange()
                .expectStatus().isNotFound().expectBody()
                .jsonPath("$.message").isEqualTo("The teacher is not found");
    }

    @Test
    void test_get_students_by_teacher_error_server() {
        String teacherId = "2392912";
        webTestClient.get()
                .uri("/student/{teacherId}/studentsByTeacher", teacherId)
                .exchange()
                .expectStatus().is5xxServerError().expectBody()
                .jsonPath("$.message").isEqualTo("Invalid UUID string: 2392912");

    }
}
