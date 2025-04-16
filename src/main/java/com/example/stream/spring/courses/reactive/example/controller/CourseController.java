package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CourseRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CourseResponseDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.service.CourseService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/getAllCourse")
    public Flux<CourseResponseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/getCourse")
    public Mono<CourseResponseDto> getCourse(@RequestParam String courseId) {
        return courseService.getCourse(courseId);
    }

    @PostMapping("/addCourse")
    public Mono<ResponseEntity<CourseResponseDto>> addCourse(@RequestBody CourseRequestDto courseDto) {
        return courseService.addCourse(courseDto)
                .map(courseResponseDto -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(courseResponseDto));
    }

    @DeleteMapping("/deleteCourse")
    public Mono<Void> deleteCourse(@RequestParam String courseId) {
        return courseService.delete(courseId);
    }

    @PutMapping("/updateCourse")
    public Mono<CourseResponseDto> updateCourse(
            @Parameter(description = "ID of the course to be updated") @RequestParam String courseId,
            @RequestBody CourseRequestDto courseDto) {
        return courseService.updateCourse(courseId, courseDto);
    }

    @GetMapping("/{courseId}/students")
    public Flux<StudentResponseDto> getStudentsByCourse(@PathVariable String courseId) {
        return courseService.getStudentsByCourseId(courseId);
    }
}
