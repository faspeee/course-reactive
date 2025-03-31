package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.ClassroomRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.ClassroomResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing classroom-related operations.
 */
@RestController
@RequestMapping("/classroom")
@Tag(name = "Classroom Management", description = "Endpoints for managing classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    /**
     * Constructs a {@code ClassroomController} with the specified {@code ClassroomService}.
     *
     * @param classroomService the service handling classroom operations
     */
    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    /**
     * Retrieves all departments.
     *
     * @return a {@link Flux} emitting all {@link ClassroomResponseDto}s
     */
    @Operation(summary = "Retrieve all departments", description = "Fetches all departments available in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of departments",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClassroomResponseDto.class)))
    @GetMapping("/getAllDepartment")
    public Flux<ClassroomResponseDto> findAllDepartments() {
        return classroomService.getAllDepartments();
    }

    /**
     * Retrieves a department by its unique identifier.
     *
     * @param departmentId the unique identifier of the department
     * @return a {@link Mono} emitting the {@link ClassroomResponseDto} if found
     */
    @Operation(summary = "Retrieve a department by ID", description = "Fetches a department based on its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the department",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClassroomResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    @GetMapping(value = "/getDepartment", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ClassroomResponseDto>> getClassroom(
            @Parameter(description = "ID of the department to be retrieved") @RequestParam("departmentId") long departmentId) {
        return classroomService.getDepartmentById(departmentId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Adds a new department.
     *
     * @param classroomRequestDto the data transfer object containing department details
     * @return a {@link Mono} emitting the created {@link ClassroomResponseDto}
     */
    @Operation(summary = "Add a new department", description = "Creates a new department with the provided details.")
    @ApiResponse(responseCode = "201", description = "Department successfully created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClassroomResponseDto.class)))
    @PostMapping(value = "/addDepartment", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClassroomResponseDto> addClassroom(
            @Parameter(description = "Department details for the new department") @RequestBody ClassroomRequestDto classroomRequestDto) {
        return classroomService.addDepartment(classroomRequestDto);
    }

    /**
     * Updates an existing department.
     *
     * @param classroomRequestDto the data transfer object containing updated department details
     * @return a {@link Mono} emitting the updated {@link ClassroomResponseDto}
     */
    @Operation(summary = "Update an existing department", description = "Updates the details of an existing department.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClassroomResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    @PutMapping(value = "/updateDepartment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ClassroomResponseDto>> updateClassroom(
            @Parameter(description = "Updated department details") @RequestBody ClassroomRequestDto classroomRequestDto) {
        return classroomService.updateDepartment(classroomRequestDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a department by its unique identifier.
     *
     * @param classroomId the unique identifier of the department to delete
     * @return a {@link Mono} that completes when the deletion is done
     */
    @Operation(summary = "Delete a department by ID", description = "Removes a department from the system based on its unique identifier.")
    @ApiResponse(responseCode = "204", description = "Department successfully deleted")
    @DeleteMapping("/deleteDepartment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteClassroom(
            @Parameter(description = "ID of the department to be deleted") @RequestParam("classroomId") long classroomId) {
        return classroomService.deleteDepartment(classroomId);
    }
}
