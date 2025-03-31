package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.College;
import com.example.stream.spring.courses.reactive.example.model.request.CollegeRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CollegeResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CollegeConverter implements Converter<CollegeRequestDto, CollegeResponseDto, College> {
    @Override
    public CollegeResponseDto toDto(College entity) {
        return new CollegeResponseDto(entity.getName(), entity.getDean(), entity.getUniversityId(), entity.getCreatedAt(),
                entity.getUpdatedAt(), entity.getIdentifier());
    }

    @Override
    public College toEntity(CollegeRequestDto dto) {
        return null;
    }
}
