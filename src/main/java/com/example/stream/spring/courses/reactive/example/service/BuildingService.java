package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.BuildingConverter;
import com.example.stream.spring.courses.reactive.example.model.response.BuildingResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.BuildingRepository;
import com.example.stream.spring.courses.reactive.example.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final BuildingConverter buildingConverter;


    public BuildingService(BuildingRepository buildingRepository, EnrollmentRepository enrollmentRepository,
                           BuildingConverter buildingConverter) {
        this.buildingRepository = buildingRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.buildingConverter = buildingConverter;
    }

    public Flux<BuildingResponseDto> getAllBuildings() {
        return buildingRepository.findAll().map(buildingConverter::toDto);
    }

    public Mono<BuildingResponseDto> getBuildingById(long buildingId) {
        return buildingRepository.findById(buildingId).map(buildingConverter::toDto);
    }


}
