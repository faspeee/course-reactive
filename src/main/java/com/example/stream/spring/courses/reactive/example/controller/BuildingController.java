package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.BuildingRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.BuildingResponseDto;
import com.example.stream.spring.courses.reactive.example.service.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/building")
public class BuildingController {

    final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/getAllBuilding")
    public Flux<BuildingResponseDto> getAllBuilding() {
        return buildingService.getAllBuildings();
    }

    @GetMapping("/getBuildingById")
    public Mono<BuildingResponseDto> getBuildingById(@RequestParam long buildingId) {
        return buildingService.getBuildingById(buildingId);
    }

    @PostMapping("/addBuilding")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BuildingResponseDto> createBuilding(@RequestBody BuildingRequestDto buildingRequestDto) {
        return buildingService.createBuilding(buildingRequestDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));
    }

    // todo: inserire cancellazione a cascata nelle dipendenze in db
    @DeleteMapping("/deleteBuilding")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBuilding(@RequestParam long buildingId) {
        return buildingService.deleteBuilding(buildingId);
    }

    @PutMapping("/updateBuilding")
    public Mono<BuildingResponseDto> updateBuilding(@RequestParam long buildingId,
                                                    @RequestBody BuildingRequestDto building) {
        return buildingService.updateBuilding(buildingId, building)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
