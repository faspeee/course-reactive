package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Classroom;
import com.example.stream.spring.courses.reactive.example.model.request.ClassroomRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.ClassroomResponseDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClassroomConverter implements Converter<ClassroomRequestDto, ClassroomResponseDto, Classroom> {
    @Override
    public ClassroomResponseDto toDto(Classroom entity) {
        return new ClassroomResponseDto(entity.getId().toString(), entity.getBuildingId().toString(), entity.getRoomNumber(),
                entity.getCapacity(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getIdentifier());
    }

    @Override
    public Classroom toEntity(ClassroomRequestDto dto) {
        Classroom classroom = new Classroom();
        classroom.setCapacity(dto.capacity());
        classroom.setIdentifier(dto.identifier());
        classroom.setRoomNumber(dto.roomNumber());
        classroom.setBuildingId(UUID.fromString(dto.buildingId()));
        return classroom;
    }
}
