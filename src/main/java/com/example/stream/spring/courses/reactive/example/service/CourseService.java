package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.Converter;
import com.example.stream.spring.courses.reactive.example.converter.CourseConverter;
import com.example.stream.spring.courses.reactive.example.entity.Course;
import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import com.example.stream.spring.courses.reactive.example.repository.CourseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseService {
  private final CourseRepository courseRepository;
  private final Converter<CourseDto, Course> converter;

  public CourseService(CourseRepository courseRepository, CourseConverter converter) {
    this.courseRepository = courseRepository;
    this.converter = converter;
  }

  public Flux<CourseDto> getAllCourses() {
    return courseRepository.findAll()
      .map(converter::toDto);
  }

  public Mono<CourseDto> addCourse(CourseDto courseDto) {
    return courseRepository.save(converter.toEntity(courseDto))
      .map(converter::toDto);
  }

  public Mono<CourseDto> updateCourse(CourseDto courseDto) {
    return courseRepository.save(converter.toEntity(courseDto))
      .map(converter::toDto);
  }

  public Mono<Void> delete(long idCourse) {
    return courseRepository.deleteById(idCourse);
  }

  public Mono<CourseDto> getCourse(long courseId) {
    return courseRepository.findById(courseId)
      .map(converter::toDto);
  }
}

