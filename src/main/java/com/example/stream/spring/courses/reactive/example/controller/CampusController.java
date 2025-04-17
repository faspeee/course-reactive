package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CampusRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CampusResponseDto;
import com.example.stream.spring.courses.reactive.example.service.CampusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing campus-related operations.
 */
@Tag(name = "Campus Management", description = "Endpoints for managing campuses")
@RestController
@RequestMapping("/campus")
public class CampusController {

    private final CampusService campusService;

    /**
     * Constructs a {@code CampusController} with the specified {@code CampusService}.
     *
     * @param campusService the service handling campus operations
     */
    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    /**
     * Retrieves all campuses.
     *
     * @return a {@link ResponseEntity} containing a {@link Flux} emitting all {@link CampusResponseDto}s
     */
    @Operation(summary = "Retrieve all campuses", description = "Fetches details of all available campuses.",
            responses = {@ApiResponse(responseCode = "200", description = "List of campuses retrieved successfully")})
    @GetMapping("/getAllCampus")
    public ResponseEntity<Flux<CampusResponseDto>> getAllCampus() {
        return ResponseEntity.ok().body(campusService.getAllCampus());
    }

    /**
     * Retrieves a campus by its unique identifier.
     *
     * @param campusId the unique identifier of the campus
     * @return a {@link ResponseEntity} containing a {@link Mono} emitting the {@link CampusResponseDto} if found
     */
    @Operation(summary = "Retrieve a campus by its ID", description = "Fetches a campus's details using its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus found"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    @GetMapping("/getCampus")
    public ResponseEntity<Mono<CampusResponseDto>> getCampus(
            @Parameter(description = "ID of the campus to be retrieved") @RequestParam String campusId) {
        return ResponseEntity.ok().body(campusService.getCampusById(campusId));
    }

    /**
     * Adds a new campus.
     *
     * @param campusRequestDto the data transfer object containing campus details
     * @return a {@link ResponseEntity} containing a {@link Mono} emitting the created {@link CampusResponseDto}
     */
    @Operation(summary = "Add a new campus", description = "Creates a new campus with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Campus created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/addCampus")
    public ResponseEntity<Mono<CampusResponseDto>> addCampus(@RequestBody CampusRequestDto campusRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(campusService.addCampus(campusRequestDto));
    }

    /**
     * Updates an existing campus.
     *
     * @param campusRequestDto the data transfer object containing updated campus details
     * @return a {@link ResponseEntity} containing a {@link Mono} emitting the updated {@link CampusResponseDto}
     */
    @Operation(summary = "Update an existing campus", description = "Updates the details of an existing campus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus updated successfully"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    @PutMapping("/updateCampus")
    public ResponseEntity<Mono<CampusResponseDto>> updateCampus(
            @Parameter(description = "ID of the campus to be updated") @RequestParam String campusId,
            @Parameter(description = "Updated campus details") @RequestBody CampusRequestDto campusRequestDto) {
        return ResponseEntity.ok().body(campusService.updateCampus(campusId, campusRequestDto));
    }

    /**
     * Deletes a campus by its unique identifier.
     *
     * @param campusId the unique identifier of the campus to delete
     * @return a {@link ResponseEntity} containing a {@link Mono} that completes when the deletion is done
     */
    @Operation(summary = "Delete a campus by its ID", description = "Removes a campus from the system using its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Campus deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Campus not found")
    })
    @DeleteMapping("/deleteCampus")
    public ResponseEntity<Mono<Void>> deleteCampus(
            @Parameter(description = "ID of the campus to be deleted") @RequestParam String campusId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(campusService.deleteCampus(campusId));
    }
}