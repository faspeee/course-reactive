package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.DepartmentConverter;
import com.example.stream.spring.courses.reactive.example.entity.Department;
import com.example.stream.spring.courses.reactive.example.model.request.DepartmentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.DepartmentResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.DepartmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentConverter departmentConverter;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentConverter departmentConverter) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
    }

    public Flux<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll()
                .map(departmentConverter::toDto);
    }

    public Mono<DepartmentResponseDto> createDepartment(DepartmentRequestDto departmentRequestDto) {
        return departmentRepository.save(departmentConverter.toEntity(departmentRequestDto))
                .map(departmentConverter::toDto);
    }

    public Mono<DepartmentResponseDto> getDepartmentById(String departmentId) {
        return getDepartmentById(UUID.fromString(departmentId))
                .map(departmentConverter::toDto);
    }

    private Department updateDepartment(Department department, DepartmentRequestDto departmentRequestDto) {
        department.setCollegeId(UUID.fromString(departmentRequestDto.collegeId()));
        department.setName(departmentRequestDto.name());
        department.setIdentifier(departmentRequestDto.identifier());
        department.setDescription(departmentRequestDto.description());
        return department;
    }

    private Mono<Department> getDepartmentById(UUID departmentId) {
        return departmentRepository.findById(departmentId);
    }

    public Mono<DepartmentResponseDto> updateDepartment(String departmentId, DepartmentRequestDto departmentRequestDto) {
        return getDepartmentById(UUID.fromString(departmentId))
                .map(department -> updateDepartment(department, departmentRequestDto))
                .flatMap(department -> departmentRepository.save(department)
                        .map(departmentConverter::toDto))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "department not found")));
    }

    public Mono<Void> deleteDepartment(String departmentId) {
        return departmentRepository.deleteById(UUID.fromString(departmentId));
    }
}
