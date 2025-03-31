package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Classroom;
import com.example.stream.spring.courses.reactive.example.model.request.ClassroomRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.ClassroomResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ClassroomConverter implements Converter<ClassroomRequestDto, ClassroomResponseDto, Classroom> {
    @Override
    public ClassroomResponseDto toDto(Classroom entity) {
        return null;
    }

    @Override
    public Classroom toEntity(ClassroomRequestDto dto) {
        return null;
    }

    @Override
    public Classroom toEntity(Long id, ClassroomRequestDto dto) {
        Classroom classroom = toEntity(dto);
        classroom.setId(id);
        return classroom;
    }
}
