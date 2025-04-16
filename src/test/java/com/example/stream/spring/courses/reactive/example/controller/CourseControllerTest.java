package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CourseRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CourseResponseDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import org.junit.jupiter.api.DisplayName;
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
    void find_all() {
        webTestClient.get().uri("/courses/getAllCourse")
                .exchange().expectStatus().isOk();
    }

    @Test
    void test_get_course() {
        String courseId = "cb3dc0a6-3bab-462d-ae69-2ad134495def"; // Replace with a valid course ID
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
    void test_add_course() {
        CourseRequestDto newCourse = createCourseDto("New Course", "code course", "id 2", 1, "88af4487-6120-4a7f-81fe-a9ebc1072413");
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

    @DisplayName("add course in department not found throw error")
    @Test
    void test_add_course_error() {
        CourseRequestDto newCourse = createCourseDto("New Course", "code course", "id 2", 1, "88af4487-6110-4a7f-81fe-a9ebc1072413");
        // Set other properties as needed
        webTestClient.post().uri("/courses/addCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCourse)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("The department is not found");
    }

    @DisplayName("add course in department not found throw error")
    @Test
    void test_add_course_internal_error() {
        CourseRequestDto newCourse = createCourseDto("New Course", "code course", "id 2", 1, "2u28i2L");
        // Set other properties as needed

        webTestClient.post().uri("/courses/addCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCourse)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void test_delete_course() {
        String courseId = "7e73ad23-266d-4746-b948-1f78e722cf02"; // Replace with a valid course ID to delete
        webTestClient.delete().uri(uriBuilder -> uriBuilder.path("/courses/deleteCourse")
                        .queryParam("courseId", courseId)
                        .build())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void test_update_course() {

        CourseRequestDto newCourse = createCourseDto("New Course", "code course", "id 2", 1, "88af4487-6120-4a7f-81fe-a9ebc1072413");
        // Set other properties as needed

        String courseId = webTestClient.post().uri("/courses/addCourse")
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
                }).returnResult().getResponseBody().courseId();

        CourseRequestDto existingCourse = createCourseDto("Updated Course Name", "code course", "id", 2, "88af4487-6120-4a7f-81fe-a9ebc1072413");

        // Set other properties as needed

        webTestClient.put().uri("/courses/updateCourse?courseId=" + courseId)
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
    void test_get_students_by_course() {
        String courseId = "7e73ad23-266d-4746-b948-1f78e722cf02"; // Replace with a valid course ID
        webTestClient.get().uri("/courses/{courseId}/students", courseId)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(StudentResponseDto.class)
                .hasSize(5); // Adjust based on expected data
    }
}
