package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import com.example.stream.spring.courses.reactive.example.service.CourseService;
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
  public Mono<CourseDto> addCourse(@RequestBody CourseDto courseDto) {
    return courseService.addCourse(courseDto);
  }

  @DeleteMapping("/deleteCourse")
  public Mono<Void> deleteCourse(@RequestParam long courseId) {
    return courseService.delete(courseId);
  }

  @PutMapping("/updateCourse")
  public Mono<CourseDto> updateCourse(@RequestBody CourseDto courseDto) {
    return courseService.updateCourse(courseDto);
  }
}
