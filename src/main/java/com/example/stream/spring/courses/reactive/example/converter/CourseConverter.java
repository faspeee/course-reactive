package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Course;
import com.example.stream.spring.courses.reactive.example.model.CourseDto;
import org.springframework.stereotype.Component;

@Component
public class CourseConverter implements Converter<CourseDto, Course> {
    @Override
    public CourseDto toDto(Course entity) {
        return new CourseDto(entity.getCourseName(), entity.getCourseCode(), entity.getStartDate(), entity.getEndDate(), entity.getCreditHours(), entity.getDepartmentId());
    }

    @Override
    public Course toEntity(CourseDto dto) {
        Course course = new Course();
        course.setCourseCode(dto.courseCode());
        course.setCourseName(dto.courseName());
        course.setCreditHours(dto.creditHours());
        course.setStartDate(dto.startDate());
        course.setEndDate(dto.endDate());
        course.setDepartmentId(dto.departmentId());
        return course;
    }
}
