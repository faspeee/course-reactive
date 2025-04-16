package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Department;
import com.example.stream.spring.courses.reactive.example.model.request.DepartmentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.DepartmentResponseDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DepartmentConverter implements Converter<DepartmentRequestDto, DepartmentResponseDto, Department> {
    @Override
    public DepartmentResponseDto toDto(Department entity) {
        return new DepartmentResponseDto(entity.getId().toString(), entity.getName(), entity.getDescription(), entity.getIdentifier());
    }

    @Override
    public Department toEntity(DepartmentRequestDto dto) {
        Department department = new Department();
        department.setName(dto.name());
        department.setDescription(dto.description());
        department.setIdentifier(dto.identifier());
        department.setCollegeId(UUID.fromString(dto.collegeId()));
        return department;
    }
}
