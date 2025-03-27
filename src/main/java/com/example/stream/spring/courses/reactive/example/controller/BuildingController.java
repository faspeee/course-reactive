package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.response.BuildingResponseDto;
import com.example.stream.spring.courses.reactive.example.service.BuildingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Flux<BuildingResponseDto>> getAllBuilding() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    @GetMapping("/getBuildingById")
    public ResponseEntity<Mono<BuildingResponseDto>> getBuildingById(@RequestParam long buildingId) {
        return ResponseEntity.ok(buildingService.getBuildingById(buildingId));
    }


}
