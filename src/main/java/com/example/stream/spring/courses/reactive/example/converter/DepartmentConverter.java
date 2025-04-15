package com.example.stream.spring.courses.reactive.example.converter;

import com.example.stream.spring.courses.reactive.example.entity.Department;
import com.example.stream.spring.courses.reactive.example.model.request.DepartmentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.DepartmentResponseDto;
import org.springframework.stereotype.Component;

@Component
public class DepartmentConverter implements Converter<DepartmentRequestDto, DepartmentResponseDto, Department> {
    @Override
    public DepartmentResponseDto toDto(Department entity) {
        return null;
    }

    @Override
    public Department toEntity(DepartmentRequestDto dto) {
        return null;
    }
}
