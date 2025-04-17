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

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final CampusRepository campusRepository;
    private final BuildingConverter buildingConverter;


    public BuildingService(BuildingRepository buildingRepository, CampusRepository campusRepository, BuildingConverter buildingConverter) {
        this.buildingRepository = buildingRepository;
        this.campusRepository = campusRepository;
        this.buildingConverter = buildingConverter;
    }

    public Flux<BuildingResponseDto> getAllBuildings() {
        return buildingRepository.findAll()
                .map(buildingConverter::toDto);
    }

    public Mono<Either<Error, BuildingResponseDto>> getBuildingById(String buildingId) {
        return findBuildingById(buildingId)
                .map(errorBuildingEither -> errorBuildingEither
                        .map(buildingConverter::toDto));
    }

    private Mono<Either<Error, Campus>> findCampusById(String campusId) {
        return campusRepository.findById(UUID.fromString(campusId))
                .<Either<Error, Campus>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new CampusNotFound())));
    }

    public Mono<Either<Error, BuildingResponseDto>> createBuilding(BuildingRequestDto newBuilding) {
        return findCampusById(newBuilding.campusId())
                .flatMap(either -> either.getRight()
                        .map(present -> buildingRepository.save(buildingConverter.toEntity(newBuilding))
                                .<Either<Error, BuildingResponseDto>>map(building -> Either.right(buildingConverter.toDto(building))))
                        .orElse(Mono.just(Either.left(either.getLeft().orElse(new GenericError())))));
    }

    public Mono<Either<Error, Success>> deleteBuilding(String buildingId) {
        return existBuildingById(buildingId)
                .flatMap(errorBuildingEither ->
                        errorBuildingEither.getRight()
                                .<Mono<Either<Error, Success>>>map(building ->
                                        buildingRepository.deleteById(UUID.fromString(buildingId))
                                                .then(Mono.just(Either.right(new BuildingDeleteOk()))))
                                .orElse(Mono.just(Either.left(errorBuildingEither.getLeft().orElse(new GenericError())))));
    }

    private Mono<Either<Error, Boolean>> existBuildingById(String buildingId) {
        return buildingRepository.existsById(UUID.fromString(buildingId))
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new GenericError())));
    }

    private Mono<Either<Error, Building>> findBuildingById(String buildingId) {
        return buildingRepository.findById(UUID.fromString(buildingId))
                .<Either<Error, Building>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new BuildingNotFound())));
    }

    private Building updateBuilding(Building building, BuildingRequestDto newBuilding) {
        building.setName(newBuilding.name());
        building.setCode(newBuilding.code());
        building.setIdentifier(newBuilding.identifier());
        building.setCampusId(UUID.fromString(newBuilding.campusId()));
        return building;
    }

    public Mono<Either<Error, BuildingResponseDto>> updateBuilding(String buildingId, BuildingRequestDto building) {
        return findBuildingById(buildingId)
                .flatMap(errorBuildingEither -> errorBuildingEither.getRight()
                        .map(oldBuilding -> {
                            String campusId = building.campusId();
                            if (!campusId.equals(oldBuilding.getCampusId().toString())) {
                                // Se il campus è cambiato, verifichiamo se esiste
                                return campusRepository.existsById(UUID.fromString(campusId))
                                        .filter(isPresent -> isPresent)
                                        .flatMap(campus -> saveOrUpdateBuilding(oldBuilding, building))
                                        .switchIfEmpty(Mono.just(Either.left(new CampusNotFound())));
                            }
                            // Se il campus non è cambiato, aggiorniamo direttamente l'edificio
                            return saveOrUpdateBuilding(oldBuilding, building);
                        })
                        .orElse(Mono.just(Either.left(errorBuildingEither.getLeft()
                                .orElse(new GenericError()))))

                );
    }

    private Mono<Either<Error, BuildingResponseDto>> saveOrUpdateBuilding(Building oldBuilding, BuildingRequestDto newBuilding) {
        return buildingRepository.save(updateBuilding(oldBuilding, newBuilding))
                .<Either<Error, BuildingResponseDto>>map(building -> Either.right(buildingConverter.toDto(building)))
                .switchIfEmpty(Mono.just(Either.left(new CampusServerError())));
    }
}
