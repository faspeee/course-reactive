package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Campus;
import com.example.stream.spring.courses.reactive.example.model.request.CampusRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CampusResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CampusConverter implements Converter<CampusRequestDto, CampusResponseDto, Campus> {
    @Override
    public CampusResponseDto toDto(Campus entity) {
        return new CampusResponseDto(entity.getName(), entity.getUniversityId(), entity.getCountry(), entity.getCity(),
                entity.getStartDate(), entity.getEndDate(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getName());
    }

    @Override
    public Campus toEntity(CampusRequestDto dto) {
        Campus entity = new Campus();
        entity.setAddress(dto.address());
        entity.setUniversityId(dto.universityId());
        entity.setCountry(dto.country());
        entity.setCity(dto.city());
        //TODO: understand when is update and when is create
        return null;
    }
}
