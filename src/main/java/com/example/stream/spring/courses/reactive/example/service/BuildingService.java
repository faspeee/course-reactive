package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.BuildingConverter;
import com.example.stream.spring.courses.reactive.example.model.request.BuildingRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.BuildingResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.BuildingRepository;
import com.example.stream.spring.courses.reactive.example.repository.CampusRepository;
import com.example.stream.spring.courses.reactive.example.repository.EnrollmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final CampusRepository campusRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final BuildingConverter buildingConverter;


    public BuildingService(BuildingRepository buildingRepository, CampusRepository campusRepository,
                           EnrollmentRepository enrollmentRepository, BuildingConverter buildingConverter) {
        this.buildingRepository = buildingRepository;
        this.campusRepository = campusRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.buildingConverter = buildingConverter;
    }

    public Flux<BuildingResponseDto> getAllBuildings() {
        return buildingRepository.findAll().map(buildingConverter::toDto);
    }

    public Mono<BuildingResponseDto> getBuildingById(long buildingId) {
        return buildingRepository.findById(buildingId).map(buildingConverter::toDto);
    }

    public Mono<BuildingResponseDto> createBuilding(BuildingRequestDto newBuilding) {
        return campusRepository.findById(newBuilding.campusId())
                .flatMap(campus -> buildingRepository.save(buildingConverter.toEntity(newBuilding))
                        .map(buildingConverter::toDto));
    }

    public Mono<Void> deleteBuilding(long buildingId) {
        return buildingRepository.findById(buildingId).flatMap(buildingRepository::delete);
    }

    public Mono<BuildingResponseDto> updateBuilding(Long buildingId, BuildingRequestDto building) {
        return buildingRepository.findById(buildingId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Building not found")))
                .flatMap(oldBuilding -> {
                    Long campusId = building.campusId();

                    if (!campusId.equals(oldBuilding.getCampusId())) {
                        // Se il campus è cambiato, verifichiamo se esiste
                        return campusRepository.findById(campusId)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campus not found")))
                                .flatMap(campus -> buildingRepository.save(buildingConverter.toEntity(buildingId, building))
                                        .map(buildingConverter::toDto)
                                        .doOnError(error -> Mono.error(
                                                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                        "Failed to save building")))
                                );
                    }

                    // Se il campus non è cambiato, aggiorniamo direttamente l'edificio
                    return buildingRepository.save(buildingConverter.toEntity(buildingId, building))
                            .map(buildingConverter::toDto)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save building")));
                });
    }
}
