package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CourseRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CourseResponseDto;
import com.example.stream.spring.courses.reactive.example.service.CourseService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processEmptyResponse;
import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processTheResultFromService;

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
    public Mono<Optional<CourseResponseDto>> getCourse(@RequestParam String courseId) {
        return processTheResultFromService(courseService.getCourseById(courseId));
    }

    @PostMapping("/addCourse")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Optional<CourseResponseDto>> addCourse(@RequestBody CourseRequestDto courseDto) {
        return processTheResultFromService(courseService.addCourse(courseDto));
    }

    @DeleteMapping("/deleteCourse")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCourse(@RequestParam String courseId) {
        return processEmptyResponse(courseService.deleteCourseById(courseId));
    }

    @PutMapping("/updateCourse")
    public Mono<Optional<CourseResponseDto>> updateCourse(
            @Parameter(description = "ID of the course to be updated") @RequestParam String courseId,
            @RequestBody CourseRequestDto courseDto) {
        return processTheResultFromService(courseService.updateCourse(courseId, courseDto));
    }

    @GetMapping("/{departmentId}/course")
    public Flux<CourseResponseDto> getCourseByDepartmentId(@PathVariable String departmentId) {
        return courseService.getCourseByDepartmentId(departmentId);
    }
}
