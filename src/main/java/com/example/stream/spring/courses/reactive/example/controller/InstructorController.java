package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.InstructorRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.InstructorResponseDto;
import com.example.stream.spring.courses.reactive.example.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing instructor-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting instructors.
 */
@RestController
@RequestMapping("/teacher")
@Tag(name = "Teacher Management", description = "Endpoints for managing teachers and their associated students")
public class InstructorController {

    private final InstructorService instructorService;

    /**
     * Constructs an InstructorController with the specified InstructorService.
     *
     * @param instructorService the service to handle instructor-related business logic
     */
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    /**
     * Retrieves all instructors in the system.
     *
     * @return a {@link Flux} of {@link InstructorResponseDto} objects
     */
    @GetMapping("/getAllInstructors")
    @Operation(
            summary = "Get all instructors",
            description = "Retrieves a list of all instructors registered in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of instructors retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InstructorResponseDto.class)))
            }
    )
    public Flux<InstructorResponseDto> getAllInstructors() {
        return instructorService.getAllInstructor();
    }

    /**
     * Retrieves an instructor by their unique identifier.
     *
     * @param instructorId the ID of the instructor to retrieve
     * @return a {@link Mono} of {@link InstructorResponseDto} if found
     */
    @GetMapping("/getInstructorById")
    @Operation(
            summary = "Get instructor by ID",
            description = "Retrieves a specific instructor using their UUID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Instructor found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InstructorResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Instructor not found", content = @Content)
            }
    )
    public Mono<InstructorResponseDto> getInstructorById(
            @Parameter(description = "UUID of the instructor") @RequestParam String instructorId) {
        return instructorService.getInstructorById(instructorId);
    }

    /**
     * Adds a new instructor to the system.
     *
     * @param requestDto the request body containing instructor details
     * @return the created {@link InstructorResponseDto}
     */
    @PostMapping("/addInstructor")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add a new instructor",
            description = "Creates a new instructor based on the provided input",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Instructor created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InstructorResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
            }
    )
    public Mono<InstructorResponseDto> addInstructor(
            @Valid @RequestBody InstructorRequestDto requestDto) {
        return instructorService.createInstructor(requestDto);
    }

    /**
     * Updates an existing instructor with new values.
     *
     * @param instructorId the UUID of the instructor to update
     * @param requestDto   the updated data
     * @return the updated {@link InstructorResponseDto}
     */
    @PutMapping("/updateInstructor")
    @Operation(
            summary = "Update an instructor",
            description = "Updates instructor information using the provided data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Instructor updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = InstructorResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Instructor not found", content = @Content)
            }
    )
    public Mono<InstructorResponseDto> updateInstructor(
            @Parameter(description = "UUID of the instructor to update") @RequestParam String instructorId,
            @Valid @RequestBody InstructorRequestDto requestDto) {
        return instructorService.updateInstructor(instructorId, requestDto);
    }

    /**
     * Deletes an instructor from the system.
     *
     * @param instructorId the UUID of the instructor to delete
     * @return a {@link Mono} that completes when deletion is finished
     */
    @DeleteMapping("/deleteInstructor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete an instructor",
            description = "Removes an instructor by their UUID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Instructor deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Instructor not found", content = @Content)
            }
    )
    public Mono<Void> deleteInstructor(
            @Parameter(description = "UUID of the instructor to delete") @RequestParam String instructorId) {
        return instructorService.deleteInstructor(instructorId);
    }
}
