package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import com.example.stream.spring.courses.reactive.example.model.StudentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    private static CourseDto createCourseDto(String courseName, String courseCode, LocalDate startDate, LocalDate endDate, int creditHours, long departmentId) {
        return new CourseDto(courseName, courseCode, startDate, endDate, creditHours, departmentId);
    }

    @Test
    public void dummyTest() {
        assertNotNull(webTestClient);
    }

    @Test
    public void findAll() {
        webTestClient.get().uri("/courses/getAllCourse")
                .exchange().expectStatus().isOk();
    }

    @Test
    public void testGetCourse() {
        long courseId = 1; // Replace with a valid course ID
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/courses/getCourse")
                        .queryParam("courseId", courseId)
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
    public void testAddCourse() {
        CourseDto newCourse = createCourseDto("New Course", "code course", LocalDate.now(), LocalDate.now(), 1, 1);
        // Set other properties as needed

        webTestClient.post().uri("/courses/addCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCourse)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CourseDto.class)
                .consumeWith(response -> {
                    CourseDto createdCourse = response.getResponseBody();
                    assertNotNull(createdCourse);
                    assertNotNull(createdCourse.courseCode());
                    assertEquals("New Course", createdCourse.courseName());
                });
    }

    @Test
    public void testDeleteCourse() {
        long courseId = 1; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/courses/deleteCourse")
                        .queryParam("courseId", courseId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void testUpdateCourse() {
        CourseDto existingCourse = createCourseDto("Updated Course Name", "code course", LocalDate.now(), LocalDate.now(), 2, 2);

        // Set other properties as needed

        webTestClient.put().uri("/courses/updateCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existingCourse)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CourseDto.class)
                .consumeWith(response -> {
                    CourseDto updatedCourse = response.getResponseBody();
                    assertNotNull(updatedCourse);
                    assertEquals("Updated Course Name", updatedCourse.courseName());
                });
    }

    @Test
    public void testGetStudentsByCourse() {
        long courseId = 1; // Replace with a valid course ID
        webTestClient.get().uri("/courses/{courseId}/students", courseId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StudentDTO.class)
                .hasSize(5); // Adjust based on expected data
    }
}
