package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CourseRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CourseResponseDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static CourseRequestDto createCourseDto(String courseName, String courseCode, String identifier, int creditHours, String departmentId) {
        return new CourseRequestDto(courseName, courseCode, creditHours, departmentId, identifier);
    }

    @Test
    void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    void findAll() {
        webTestClient.get().uri("/courses/getAllCourse")
                .exchange().expectStatus().isOk();
    }

    @Test
    void testGetCourse() {
        long courseId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/courses/getCourse")
                        .queryParam("courseId", courseId)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CourseResponseDto.class)
                .consumeWith(response -> {
                    CourseResponseDto course = response.getResponseBody();
                    assertNotNull(course);
                });
    }

    @Test
    void testAddCourse() {
        CourseRequestDto newCourse = createCourseDto("New Course", "code course", "id 2", 1, "1");
        // Set other properties as needed

        webTestClient.post().uri("/courses/addCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCourse)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CourseResponseDto.class)
                .consumeWith(response -> {
                    CourseResponseDto createdCourse = response.getResponseBody();
                    assertNotNull(createdCourse);
                    assertNotNull(createdCourse.courseCode());
                    assertEquals("New Course", createdCourse.courseName());
                });
    }

    @Test
    void testDeleteCourse() {
        long courseId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/courses/deleteCourse")
                        .queryParam("courseId", courseId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateCourse() {
        CourseRequestDto existingCourse = createCourseDto("Updated Course Name", "code course", "id", 2, "2");

        // Set other properties as needed

        webTestClient.put().uri("/courses/updateCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingCourse)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CourseResponseDto.class)
                .consumeWith(response -> {
                    CourseResponseDto updatedCourse = response.getResponseBody();
                    assertNotNull(updatedCourse);
                    assertEquals("Updated Course Name", updatedCourse.courseName());
                });
    }

    @Test
    void testGetStudentsByCourse() {
        long courseId = 1; // Replace with a valid course ID
        webTestClient.get().uri("/courses/{courseId}/students", courseId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StudentResponseDto.class)
                .hasSize(5); // Adjust based on expected data
    }
}
