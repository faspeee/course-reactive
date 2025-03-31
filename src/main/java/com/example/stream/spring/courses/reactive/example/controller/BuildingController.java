package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.BuildingRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.BuildingResponseDto;
import com.example.stream.spring.courses.reactive.example.service.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing building-related operations.
 */
@Tag(name = "Building Management", description = "Endpoints for managing buildings")
@RestController
@RequestMapping("/building")
public class BuildingController {

    private final BuildingService buildingService;

    /**
     * Constructs a {@code BuildingController} with the specified {@code BuildingService}.
     *
     * @param buildingService the service handling building operations
     */
    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    /**
     * Retrieves all buildings.
     *
     * @return a {@link Flux} emitting all {@link BuildingResponseDto}s
     */
    @Operation(summary = "Retrieve all buildings", description = "Fetches details of all available buildings.")
    @ApiResponse(responseCode = "200", description = "List of buildings retrieved successfully")
    @GetMapping("/getAllBuilding")
    public Flux<BuildingResponseDto> getAllBuilding() {
        return buildingService.getAllBuildings();
    }

    /**
     * Retrieves a building by its unique identifier.
     *
     * @param buildingId the unique identifier of the building
     * @return a {@link Mono} emitting the {@link BuildingResponseDto} if found
     */
    @Operation(summary = "Retrieve a building by its ID", description = "Fetches a building's details using its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Building found"),
            @ApiResponse(responseCode = "404", description = "Building not found")
    })
    @GetMapping("/getBuildingById")
    public Mono<BuildingResponseDto> getBuildingById(
            @Parameter(description = "ID of the building to be retrieved") @RequestParam long buildingId) {
        return buildingService.getBuildingById(buildingId);
    }

    /**
     * Adds a new building.
     *
     * @param buildingRequestDto the data transfer object containing building details
     * @return a {@link Mono} emitting the created {@link BuildingResponseDto}
     */
    @Operation(summary = "Add a new building", description = "Creates a new building with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Building created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/addBuilding")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BuildingResponseDto> createBuilding(
            @Parameter(description = "Building details for the new building") @RequestBody BuildingRequestDto buildingRequestDto) {
        return buildingService.createBuilding(buildingRequestDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));
    }

    /**
     * Deletes a building by its unique identifier.
     *
     * @param buildingId the unique identifier of the building to delete
     * @return a {@link Mono} that completes when the deletion is done
     */
    @Operation(summary = "Delete a building by its ID", description = "Removes a building from the system using its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Building deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Building not found")
    })
    @DeleteMapping("/deleteBuilding")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBuilding(
            @Parameter(description = "ID of the building to be deleted") @RequestParam long buildingId) {
        return buildingService.deleteBuilding(buildingId);
    }

    /**
     * Updates an existing building.
     *
     * @param buildingId         the unique identifier of the building to update
     * @param buildingRequestDto the data transfer object containing updated building details
     * @return a {@link Mono} emitting the updated {@link BuildingResponseDto}
     */
    @Operation(summary = "Update an existing building", description = "Updates the details of an existing building.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Building updated successfully"),
            @ApiResponse(responseCode = "404", description = "Building not found")
    })
    @PutMapping("/updateBuilding")
    public Mono<BuildingResponseDto> updateBuilding(
            @Parameter(description = "ID of the building to be updated") @RequestParam long buildingId,
            @Parameter(description = "Updated building details") @RequestBody BuildingRequestDto buildingRequestDto) {
        return buildingService.updateBuilding(buildingId, buildingRequestDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
