package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CampusRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CampusResponseDto;
import com.example.stream.spring.courses.reactive.example.service.CampusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/campus")
public class CampusController {
    private final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    @GetMapping("/getAllCampus")
    public ResponseEntity<Flux<CampusResponseDto>> getAllCampus() {
        return ResponseEntity.ok().body(campusService.getAllCampus());
    }

    @GetMapping("/getCampus")
    public ResponseEntity<Mono<CampusResponseDto>> getCampus(@RequestParam Long campusId) {
        return ResponseEntity.ok().body(campusService.getCampus(campusId));
    }

    @PostMapping("/addCampus")
    public ResponseEntity<Mono<CampusResponseDto>> addCampus(@RequestBody CampusRequestDto campusRequestDto) {
        Mono<CampusResponseDto> campus = campusService.addCampus(campusRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(campus);
    }

    @DeleteMapping("/deleteCampus")
    public ResponseEntity<Mono<Void>> deleteCampus(@RequestParam Long campusId) {
        return ResponseEntity.ok().body(campusService.deleteCampus(campusId));
    }

    @PutMapping("/updateCampus")
    public ResponseEntity<Mono<CampusResponseDto>> updateCampus(@RequestBody CampusRequestDto campusRequestDto) {
        return ResponseEntity.ok().body(campusService.updateCampus(campusRequestDto));
    }
}
