package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Course;
import com.example.stream.spring.courses.reactive.example.model.request.CourseRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CourseResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CourseConverter implements Converter<CourseRequestDto, CourseResponseDto, Course> {
    @Override
    public CourseResponseDto toDto(Course entity) {
        return new CourseResponseDto(entity.getId().toString(), entity.getCourseName(), entity.getCourseCode(),
                entity.getStartDate(), entity.getEndDate(), entity.getCreditHours(),
                entity.getDepartmentId(), entity.getIdentifier());
    }

    @Override
    public Course toEntity(CourseRequestDto dto) {
        Course course = new Course();
        course.setCourseCode(dto.courseCode());
        course.setCourseName(dto.courseName());
        course.setCreditHours(dto.creditHours());
        // fixme
        //course.setStartDate(dto.);
        //course.setEndDate(dto.endDate());
        course.setDepartmentId(dto.departmentId());
        return course;
    }

}
