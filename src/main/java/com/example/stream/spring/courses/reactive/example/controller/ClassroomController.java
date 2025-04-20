package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.ClassroomRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.ClassroomResponseDto;
import com.example.stream.spring.courses.reactive.example.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processEmptyResponse;
import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processTheResultFromService;

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
     * Retrieves all classroom.
     *
     * @return a {@link Flux} emitting all {@link ClassroomResponseDto}s
     */
    @Operation(summary = "Retrieve all classroom", description = "Fetches all classroom available in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of classroom",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClassroomResponseDto.class)))
    @GetMapping("/getAllClassroom")
    public Flux<ClassroomResponseDto> findAllClassrooms() {
        return classroomService.getAllClassrooms();
    }

    /**
     * Retrieves a classroom by its unique identifier.
     *
     * @param classroomId the unique identifier of the classroom
     * @return a {@link Mono} emitting the {@link ClassroomResponseDto} if found
     */
    @Operation(summary = "Retrieve a classroom by ID", description = "Fetches a classroom based on its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the classroom",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClassroomResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "classroom not found")
            })
    @GetMapping(value = "/getClassroom", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Optional<ClassroomResponseDto>> getClassroom(
            @Parameter(description = "ID of the classroom to be retrieved") @RequestParam("classroomId") String classroomId) {
        return processTheResultFromService(classroomService.getClassroomById(classroomId));
    }

    /**
     * Adds a new classroom.
     *
     * @param classroomRequestDto the data transfer object containing classroom details
     * @return a {@link Mono} emitting the created {@link ClassroomResponseDto}
     */
    @Operation(summary = "Add a new classroom", description = "Creates a new classroom with the provided details.")
    @ApiResponse(responseCode = "201", description = "Classroom successfully created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClassroomResponseDto.class)))
    @PostMapping(value = "/addClassroom", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Optional<ClassroomResponseDto>> addClassroom(
            @Parameter(description = "Classroom details for the new department") @RequestBody ClassroomRequestDto classroomRequestDto) {
        return processTheResultFromService(classroomService.addClassroom(classroomRequestDto));
    }

    /**
     * Updates an existing classroom.
     *
     * @param classroomRequestDto the data transfer object containing updated classroom details
     * @return a {@link Mono} emitting the updated {@link ClassroomResponseDto}
     */
    @Operation(summary = "Update an existing classroom", description = "Updates the details of an existing classroom.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Classroom successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ClassroomResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Classroom not found")
            })
    @PutMapping(value = "/updateClassroom", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Optional<ClassroomResponseDto>> updateClassroom(
            @Parameter(description = "Updated classroom details") @RequestBody ClassroomRequestDto classroomRequestDto,
            @RequestParam String classroomId) {
        return processTheResultFromService(classroomService.updateClassroom(classroomId, classroomRequestDto));
    }

    /**
     * Deletes a classroom by its unique identifier.
     *
     * @param classroomId the unique identifier of the classroom to delete
     * @return a {@link Mono} that completes when the deletion is done
     */
    @Operation(summary = "Delete a classroom by ID", description = "Removes a classroom from the system based on its unique identifier.")
    @ApiResponse(responseCode = "204", description = "Classroom successfully deleted")
    @DeleteMapping("/deleteClassroom")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteClassroom(
            @Parameter(description = "ID of the classroom to be deleted") @RequestParam("classroomId") String classroomId) {
        return processEmptyResponse(classroomService.deleteClassroom(classroomId));
    }
}
