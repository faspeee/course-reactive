package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.DepartmentConverter;
import com.example.stream.spring.courses.reactive.example.entity.Department;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.DepartmentDeleteOk;
import com.example.stream.spring.courses.reactive.example.model.error.DepartmentNotFound;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.Success;
import com.example.stream.spring.courses.reactive.example.model.request.DepartmentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.DepartmentResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.stream.spring.courses.reactive.example.utility.UtilMono.createMonoWithError;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentConverter departmentConverter;
    private final CollegeService collegeService;

    public DepartmentService(DepartmentRepository departmentRepository, DepartmentConverter departmentConverter, CollegeService collegeService) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
        this.collegeService = collegeService;
    }

    public Flux<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll()
                .map(departmentConverter::toDto);
    }

    public Mono<Either<Error, DepartmentResponseDto>> createDepartment(DepartmentRequestDto departmentRequestDto) {
        return collegeService.existCollegeById(departmentRequestDto.collegeId())
                .flatMap(either -> either.getRight()
                        .map(aBoolean -> departmentRepository.save(departmentConverter.toEntity(departmentRequestDto))
                                .<Either<Error, DepartmentResponseDto>>map(department -> Either.right(departmentConverter.toDto(department))))
                        .orElse(createMonoWithError(either)));
    }

    public Mono<Either<Error, DepartmentResponseDto>> getDepartmentById(String departmentId) {
        return getDepartmentById(UUID.fromString(departmentId))
                .map(errorDepartmentEither -> errorDepartmentEither.map(departmentConverter::toDto));
    }

    private Department updateDepartment(Department department, DepartmentRequestDto departmentRequestDto) {
        department.setCollegeId(UUID.fromString(departmentRequestDto.collegeId()));
        department.setName(departmentRequestDto.name());
        department.setIdentifier(departmentRequestDto.identifier());
        department.setDescription(departmentRequestDto.description());
        return department;
    }

    private Mono<Either<Error, Department>> getDepartmentById(UUID departmentId) {
        return departmentRepository.findById(departmentId)
                .<Either<Error, Department>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new DepartmentNotFound())));
    }

    public Mono<Either<Error, DepartmentResponseDto>> updateDepartment(String departmentId, DepartmentRequestDto departmentRequestDto) {
        return getDepartmentById(UUID.fromString(departmentId))
                .flatMap(either -> either.getRight()
                        .map(department -> updateDepartment(department, departmentRequestDto))
                        .map(department -> departmentRepository.save(department)
                                .<Either<Error, DepartmentResponseDto>>map(department1 -> Either.right(departmentConverter.toDto(department1))))
                        .orElse(createMonoWithError(either)));
    }

    public Mono<Either<Error, Boolean>> existDepartmentById(String departmentId) {
        return departmentRepository.existsById(UUID.fromString(departmentId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new DepartmentNotFound())));
    }

    public Mono<Either<Error, Success>> deleteDepartment(String departmentId) {
        return existDepartmentById(departmentId)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(building ->
                                departmentRepository.deleteById(UUID.fromString(departmentId))
                                        .then(Mono.just(Either.right(new DepartmentDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }

}
