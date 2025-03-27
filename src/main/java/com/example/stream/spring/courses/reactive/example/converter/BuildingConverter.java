package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Building;
import com.example.stream.spring.courses.reactive.example.model.request.BuildingRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.BuildingResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BuildingConverter implements Converter<BuildingRequestDto, BuildingResponseDto, Building> {
    @Override
    public BuildingResponseDto toDto(Building building) {
        return new BuildingResponseDto(building.getName(), building.getCode(), building.getCampusId(),
                building.getCreatedAt(), building.getUpdatedAt(), building.getIdentifier());
    }

    @Override
    public Building toEntity(BuildingRequestDto dto) {
        Building building = new Building();
        building.setName(dto.name());
        building.setCode(dto.code());
        building.setCampusId(dto.campusId());
        building.setCreatedAt(LocalDateTime.now());
        return building;
    }

}
