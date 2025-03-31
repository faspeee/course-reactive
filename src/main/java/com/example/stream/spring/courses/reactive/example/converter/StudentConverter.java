package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Student;
import com.example.stream.spring.courses.reactive.example.model.request.StudentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import org.springframework.stereotype.Component;

/**
 * Converter class for transforming Student entities to StudentDTOs and vice versa.
 */
@Component
public class StudentConverter implements Converter<StudentRequestDto, StudentResponseDto, Student> {
    /**
     * Converts a Student entity to a StudentDTO.
     *
     * @param student the Student entity to convert
     * @return the corresponding StudentDTO
     */
    @Override
    public StudentResponseDto toDto(Student student) {
        return new StudentResponseDto(student.getName(), student.getSurname(), student.getEmail());
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public Student toEntity(StudentRequestDto dto) {
        return null;
    }

    @Override
    public Student toEntity(Long id, StudentRequestDto dto) {
        Student student = toEntity(dto);
        student.setId(id);
        return student;
    }

}