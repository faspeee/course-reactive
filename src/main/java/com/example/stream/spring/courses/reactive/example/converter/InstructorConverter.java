package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Instructor;
import com.example.stream.spring.courses.reactive.example.model.request.InstructorRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.InstructorResponseDto;
import org.springframework.stereotype.Component;

@Component
public class InstructorConverter implements Converter<InstructorRequestDto, InstructorResponseDto, Instructor> {
    @Override
    public InstructorResponseDto toDto(Instructor entity) {
        return new InstructorResponseDto(entity.getId().toString(), entity.getName(), entity.getEmail(), entity.getIdentifier(), entity.getCreatedAt(), entity.getUpdatedAt());
    }

    @Override
    public Instructor toEntity(InstructorRequestDto dto) {
        Instructor instructor = new Instructor();
        instructor.setName(dto.name());
        instructor.setEmail(dto.email());
        instructor.setIdentifier(dto.identifier());
        return instructor;
    }
}
