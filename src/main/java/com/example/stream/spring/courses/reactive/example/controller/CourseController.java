package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import com.example.stream.spring.courses.reactive.example.model.StudentDTO;
import com.example.stream.spring.courses.reactive.example.service.CourseService;
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
    public Flux<CourseDto> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/getCourse")
    public Mono<CourseDto> getCourse(@RequestParam long courseId) {
        return courseService.getCourse(courseId);
    }

    @PostMapping("/addCourse")
    public ResponseEntity<Mono<CourseDto>> addCourse(@RequestBody CourseDto courseDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseService.addCourse(courseDto));
    }

    @DeleteMapping("/deleteCourse")
    public Mono<Void> deleteCourse(@RequestParam long courseId) {
        return courseService.delete(courseId);
    }

    @PutMapping("/updateCourse")
    public Mono<CourseDto> updateCourse(@RequestBody CourseDto courseDto) {
        return courseService.updateCourse(courseDto);
    }

    @GetMapping("/{courseId}/students")
    public Flux<StudentDTO> getStudentsByCourse(@PathVariable Long courseId) {
        return courseService.getStudentsByCourseId(courseId);
    }
}
