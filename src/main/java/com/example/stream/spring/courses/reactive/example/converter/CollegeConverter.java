package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.College;
import com.example.stream.spring.courses.reactive.example.model.request.CollegeRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CollegeResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CollegeConverter implements Converter<CollegeRequestDto, CollegeResponseDto, College> {
    @Override
    public CollegeResponseDto toDto(College entity) {
        return new CollegeResponseDto(entity.getId().toString(), entity.getName(), entity.getDean(), entity.getUniversityId().toString(),
                entity.getCreatedAt(), entity.getUpdatedAt(), entity.getIdentifier());
    }

    @Override
    public College toEntity(CollegeRequestDto dto) {
        College college = new College();
        college.setDean(dto.dean());
        college.setUniversityId(UUID.fromString(dto.universityId()));
        college.setName(dto.name());
        college.setIdentifier(dto.identifier());
        college.setCreatedAt(LocalDateTime.now());
        return college;
    }
}
