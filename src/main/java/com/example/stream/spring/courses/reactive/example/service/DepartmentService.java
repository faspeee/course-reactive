package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.DepartmentConverter;
import com.example.stream.spring.courses.reactive.example.entity.College;
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

/**
 * {@code DepartmentService} encapsulates the business logic for managing {@link Department} entities.
 * <p>
 * It supports a full set of operations including creation, update, retrieval, and deletion, while ensuring
 * referential integrity with associated {@link College} entities.
 * </p>
 *
 * <p>
 * All operations are built on top of the Project Reactor stack ({@link Mono}, {@link Flux}) to support non-blocking,
 * backpressure-aware systems. Errors are managed functionally using {@code Either<Error, T>}, avoiding runtime exceptions.
 * </p>
 */
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentConverter departmentConverter;
    private final CollegeService collegeService;

    /**
     * Constructs the {@code DepartmentService} with required collaborators.
     *
     * @param departmentRepository repository for CRUD operations on Department entities
     * @param departmentConverter  converter between domain and DTO representations
     * @param collegeService       service to validate college associations
     */
    public DepartmentService(DepartmentRepository departmentRepository, DepartmentConverter departmentConverter, CollegeService collegeService) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
        this.collegeService = collegeService;
    }

    /**
     * Fetches all departments from the system.
     *
     * @return a {@link Flux} stream of {@link DepartmentResponseDto}
     */
    public Flux<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll()
                .map(departmentConverter::toDto);
    }

    /**
     * Creates a new department if the associated college exists.
     *
     * @param departmentRequestDto DTO containing new department data
     * @return a {@link Mono} of {@code Either<Error, DepartmentResponseDto>}
     */
    public Mono<Either<Error, DepartmentResponseDto>> createDepartment(DepartmentRequestDto departmentRequestDto) {
        return collegeService.existCollegeById(departmentRequestDto.collegeId())
                .flatMap(either -> either.getRight()
                        .map(aBoolean -> departmentRepository.save(departmentConverter.toEntity(departmentRequestDto))
                                .<Either<Error, DepartmentResponseDto>>map(department -> Either.right(departmentConverter.toDto(department))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Retrieves a department by its ID as a response DTO.
     *
     * @param departmentId the UUID string of the department
     * @return a {@link Mono} with {@code Either<Error, DepartmentResponseDto>}
     */
    public Mono<Either<Error, DepartmentResponseDto>> getDepartmentById(String departmentId) {
        return getDepartmentById(UUID.fromString(departmentId))
                .map(errorDepartmentEither -> errorDepartmentEither.map(departmentConverter::toDto));
    }

    /**
     * Updates the fields of a {@link Department} entity from the provided request DTO.
     *
     * @param department           existing department entity
     * @param departmentRequestDto the new department data
     * @return the updated {@link Department} entity
     */
    private Department updateDepartment(Department department, DepartmentRequestDto departmentRequestDto) {
        department.setCollegeId(UUID.fromString(departmentRequestDto.collegeId()));
        department.setName(departmentRequestDto.name());
        department.setIdentifier(departmentRequestDto.identifier());
        department.setDescription(departmentRequestDto.description());
        return department;
    }

    /**
     * Retrieves a department by ID and wraps it in an {@code Either}.
     *
     * @param departmentId UUID of the department
     * @return {@code Mono<Either<Error, Department>>}, where left is {@link DepartmentNotFound}
     */
    private Mono<Either<Error, Department>> getDepartmentById(UUID departmentId) {
        return departmentRepository.findById(departmentId)
                .<Either<Error, Department>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new DepartmentNotFound())));
    }

    /**
     * Updates a department by ID if it exists and the associated college is valid.
     *
     * @param departmentId         the ID of the department to update
     * @param departmentRequestDto the updated department data
     * @return {@link Mono} with {@code Either<Error, DepartmentResponseDto>}
     */
    public Mono<Either<Error, DepartmentResponseDto>> updateDepartment(String departmentId, DepartmentRequestDto departmentRequestDto) {
        return getDepartmentById(UUID.fromString(departmentId))
                .flatMap(either -> either.getRight()
                        .map(department -> updateDepartment(department, departmentRequestDto))
                        .map(department -> departmentRepository.save(department)
                                .<Either<Error, DepartmentResponseDto>>map(department1 -> Either.right(departmentConverter.toDto(department1))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Checks if a department exists by its ID.
     *
     * @param departmentId the ID to check
     * @return {@code Mono<Either<Error, Boolean>>}, right(true) if exists, else left(error)
     */
    public Mono<Either<Error, Boolean>> existDepartmentById(String departmentId) {
        return departmentRepository.existsById(UUID.fromString(departmentId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new DepartmentNotFound())));
    }

    /**
     * Deletes a department by ID if it exists.
     *
     * @param departmentId the ID of the department to delete
     * @return {@link Mono} with {@code Either<Error, Success>}
     */
    public Mono<Either<Error, Success>> deleteDepartment(String departmentId) {
        return existDepartmentById(departmentId)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(exists ->
                                departmentRepository.deleteById(UUID.fromString(departmentId))
                                        .then(Mono.just(Either.right(new DepartmentDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }


}
