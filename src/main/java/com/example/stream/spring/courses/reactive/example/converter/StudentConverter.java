package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Student;
import com.example.stream.spring.courses.reactive.example.model.request.StudentDto;
import org.springframework.stereotype.Component;

/**
 * Converter class for transforming Student entities to StudentDTOs and vice versa.
 */
@Component
public class StudentConverter implements Converter<StudentDto, Student> {
    /**
     * Converts a Student entity to a StudentDTO.
     *
     * @param student the Student entity to convert
     * @return the corresponding StudentDTO
     */
    @Override
    public StudentDto toDto(Student student) {
        return new StudentDto(student.getName(), student.getSurname(), student.getEmail());
    }

    /**
     * Converts a StudentDTO to a Student entity.
     *
     * @param studentDTO the StudentDTO to convert
     * @return the corresponding Student entity
     */
    @Override
    public Student toEntity(StudentDto studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.id());
        student.setName(studentDTO.name());
        student.setEmail(studentDTO.email());
        return student;
    }
}