package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CollegeRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CollegeResponseDto;
import com.example.stream.spring.courses.reactive.example.service.CollegeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processEmptyResponse;
import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processTheResultFromService;

/**
 * REST controller for managing college-related operations.
 */
@Tag(name = "College Management", description = "Endpoints for managing colleges")
@RestController
@RequestMapping("/college")
public class CollegeController {

    private final CollegeService collegeService;

    /**
     * Constructs a {@code CollegeController} with the specified {@code CollegeService}.
     *
     * @param collegeService the service handling college operations
     */
    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }


    /**
     * Retrieves a college by its unique identifier.
     *
     * @param collegeId the unique identifier of the college
     * @return a {@link Mono} emitting the {@link CollegeResponseDto} if found
     * @throws ResponseStatusException if the college is not found
     */
    @Operation(summary = "Retrieve a college by its ID", description = "Fetches a college's details using its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "College found"),
                    @ApiResponse(responseCode = "404", description = "College not found")
            })
    @GetMapping("/getCollege")
    public Mono<Optional<CollegeResponseDto>> getCollege(
            @Parameter(description = "ID of the college to be retrieved") @RequestParam String collegeId) {
        return processTheResultFromService(collegeService.getCollege(collegeId));
    }

    /**
     * Retrieves all colleges.
     *
     * @return a {@link Flux} emitting all {@link CollegeResponseDto}s
     */
    @Operation(summary = "Retrieve all colleges", description = "Fetches details of all available colleges.")
    @ApiResponse(responseCode = "200", description = "List of colleges retrieved successfully")
    @GetMapping("/getAllCollege")
    public Flux<CollegeResponseDto> getAllCollege() {
        return collegeService.getAllCollege();
    }

    /**
     * Adds a new college.
     *
     * @param collegeRequestDto the data transfer object containing college details
     * @return a {@link ResponseEntity} containing the created {@link CollegeResponseDto}
     */
    @Operation(summary = "Add a new college", description = "Creates a new college with the provided details.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "College created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addCollege")
    public Mono<Optional<CollegeResponseDto>> addCollegeDto(
            @Parameter(description = "College details for the new college") @RequestBody CollegeRequestDto collegeRequestDto) {
        return processTheResultFromService(collegeService.addCollegeDto(collegeRequestDto));
    }

    /**
     * Updates an existing college.
     *
     * @param collegeRequestDto the data transfer object containing updated college details
     * @return a {@link Mono} emitting the updated {@link CollegeResponseDto}
     */
    @Operation(summary = "Update an existing college", description = "Updates the details of an existing college.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "College updated successfully"),
                    @ApiResponse(responseCode = "404", description = "College not found")
            })
    @PutMapping("/updateCollege")
    public Mono<Optional<CollegeResponseDto>> updateCollegeDto(
            @Parameter(description = "ID of the college to be updated") @RequestParam String collegeId,
            @Parameter(description = "Updated college details") @RequestBody CollegeRequestDto collegeRequestDto) {
        return processTheResultFromService(collegeService.updateCollegeDto(collegeId, collegeRequestDto));
    }

    /**
     * Deletes a college by its unique identifier.
     *
     * @param collegeId the unique identifier of the college to delete
     * @return a {@link ResponseEntity} containing a {@link Mono} that completes when the deletion is done
     */
    @Operation(summary = "Delete a college by its ID", description = "Removes a college from the system using its unique identifier.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "College deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "College not found")
            })
    @DeleteMapping("/deleteCollege")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCollegeDto(
            @Parameter(description = "ID of the college to be deleted") @RequestParam String collegeId) {
        return processEmptyResponse(collegeService.deleteCollegeDto(collegeId));
    }
}

