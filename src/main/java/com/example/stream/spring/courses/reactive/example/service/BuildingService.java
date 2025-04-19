package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.BuildingConverter;
import com.example.stream.spring.courses.reactive.example.entity.Building;
import com.example.stream.spring.courses.reactive.example.entity.Campus;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.*;
import com.example.stream.spring.courses.reactive.example.model.request.BuildingRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.BuildingResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.BuildingRepository;
import com.example.stream.spring.courses.reactive.example.repository.CampusRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.stream.spring.courses.reactive.example.utility.UtilMono.createMonoWithError;

/**
 * Service class that encapsulates the business logic related to {@link Building} entities.
 * It provides methods for managing building data, including creating, updating, retrieving, and deleting buildings.
 *
 * <p>This class utilizes reactive programming with {@link Mono} and {@link Flux}, and functional error handling
 * using a custom {@link Either} type to gracefully handle success and error outcomes.</p>
 */
@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final CampusRepository campusRepository;
    private final BuildingConverter buildingConverter;

    /**
     * Constructs a new {@code BuildingService} with required dependencies.
     *
     * @param buildingRepository the repository for building data access
     * @param campusRepository   the repository for campus data access
     * @param buildingConverter  converter for mapping between entity and DTO
     */
    public BuildingService(BuildingRepository buildingRepository, CampusRepository campusRepository, BuildingConverter buildingConverter) {
        this.buildingRepository = buildingRepository;
        this.campusRepository = campusRepository;
        this.buildingConverter = buildingConverter;
    }

    /**
     * Retrieves all buildings from the repository and maps them to response DTOs.
     *
     * @return a {@link Flux} stream of {@link BuildingResponseDto}
     */
    public Flux<BuildingResponseDto> getAllBuildings() {
        return buildingRepository.findAll()
                .map(buildingConverter::toDto);
    }

    /**
     * Retrieves a building by its ID, wrapped in an {@link Either} structure for error handling.
     *
     * @param buildingId the UUID string of the building
     * @return a {@link Mono} containing either an error or the {@link BuildingResponseDto}
     */
    public Mono<Either<Error, BuildingResponseDto>> getBuildingById(String buildingId) {
        return findBuildingById(buildingId)
                .map(either -> either.map(buildingConverter::toDto));
    }

    /**
     * Finds a campus by its ID, returning an {@link Either} that contains either the campus or a {@link CampusNotFound} error.
     *
     * @param campusId the UUID string of the campus
     * @return a {@link Mono} of {@link Either} containing a {@link Campus} or an error
     */
    private Mono<Either<Error, Campus>> findCampusById(String campusId) {
        return campusRepository.findById(UUID.fromString(campusId))
                .<Either<Error, Campus>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new CampusNotFound())));
    }

    /**
     * Creates a new building entity after validating its campus. Returns either an error or the created building DTO.
     *
     * @param newBuilding the request DTO containing building information
     * @return a {@link Mono} of {@link Either} containing the result or an error
     */
    public Mono<Either<Error, BuildingResponseDto>> createBuilding(BuildingRequestDto newBuilding) {
        return findCampusById(newBuilding.campusId())
                .flatMap(either -> either.getRight()
                        .map(present -> buildingRepository.save(buildingConverter.toEntity(newBuilding))
                                .<Either<Error, BuildingResponseDto>>map(building -> Either.right(buildingConverter.toDto(building))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Deletes a building if it exists. Returns either a deletion success response or an error.
     *
     * @param buildingId the UUID string of the building to delete
     * @return a {@link Mono} of {@link Either} containing success or error information
     */
    public Mono<Either<Error, Success>> deleteBuilding(String buildingId) {
        return existBuildingById(buildingId)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(building ->
                                buildingRepository.deleteById(UUID.fromString(buildingId))
                                        .then(Mono.just(Either.right(new BuildingDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Checks if a building exists by its ID.
     *
     * @param buildingId the UUID string of the building
     * @return a {@link Mono} of {@link Either} containing true if found or an error if not
     */
    public Mono<Either<Error, Boolean>> existBuildingById(String buildingId) {
        return buildingRepository.existsById(UUID.fromString(buildingId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new BuildingNotFound())));
    }

    /**
     * Finds a building entity by its ID and returns it wrapped in an {@link Either}.
     *
     * @param buildingId the UUID string of the building
     * @return a {@link Mono} of {@link Either} containing the building or an error
     */
    private Mono<Either<Error, Building>> findBuildingById(String buildingId) {
        return buildingRepository.findById(UUID.fromString(buildingId))
                .<Either<Error, Building>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new BuildingNotFound())));
    }

    /**
     * Updates the values of an existing {@link Building} entity from the new request data.
     *
     * @param building    the existing building entity
     * @param newBuilding the request DTO with updated data
     * @return the updated building entity
     */
    private Building updateBuilding(Building building, BuildingRequestDto newBuilding) {
        building.setName(newBuilding.name());
        building.setCode(newBuilding.code());
        building.setIdentifier(newBuilding.identifier());
        building.setCampusId(UUID.fromString(newBuilding.campusId()));
        return building;
    }

    /**
     * Updates an existing building. If the campus is changed, checks whether the new campus exists before applying changes.
     *
     * @param buildingId the UUID string of the building to update
     * @param building   the new building request DTO
     * @return a {@link Mono} of {@link Either} containing the updated DTO or an error
     */
    public Mono<Either<Error, BuildingResponseDto>> updateBuilding(String buildingId, BuildingRequestDto building) {
        return findBuildingById(buildingId)
                .flatMap(either -> either.getRight()
                        .map(existingBuilding -> {
                            String newCampusId = building.campusId();
                            if (!newCampusId.equals(existingBuilding.getCampusId().toString())) {
                                return campusRepository.existsById(UUID.fromString(newCampusId))
                                        .filter(Boolean::booleanValue)
                                        .flatMap(valid -> saveOrUpdateBuilding(existingBuilding, building))
                                        .switchIfEmpty(Mono.just(Either.left(new CampusNotFound())));
                            }
                            return saveOrUpdateBuilding(existingBuilding, building);
                        })
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Persists the updated building and returns the updated DTO wrapped in an {@link Either}.
     *
     * @param oldBuilding the current building entity
     * @param newBuilding the request DTO containing updated values
     * @return a {@link Mono} of {@link Either} with the result or a {@link CampusServerError}
     */
    private Mono<Either<Error, BuildingResponseDto>> saveOrUpdateBuilding(Building oldBuilding, BuildingRequestDto newBuilding) {
        return buildingRepository.save(updateBuilding(oldBuilding, newBuilding))
                .<Either<Error, BuildingResponseDto>>map(building -> Either.right(buildingConverter.toDto(building)))
                .switchIfEmpty(Mono.just(Either.left(new CampusServerError())));
    }
}
