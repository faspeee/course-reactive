package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.CollegeRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CollegeResponseDto;
import com.example.stream.spring.courses.reactive.example.service.CollegeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/college")
public class CollegeController {
    private final CollegeService collegeService;

    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @GetMapping("/getCollege")
    public Mono<CollegeResponseDto> getCollege(@RequestParam long collegeId) {
        return collegeService.getCollege(collegeId)
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.NOT_FOUND, "college not found"));
    }


    @GetMapping("/getAllCollege")
    public Flux<CollegeResponseDto> getAllCollege() {
        return collegeService.getAllCollege();
    }

    @PostMapping("/addCollege")
    public ResponseEntity<Mono<CollegeResponseDto>> addCollegeDto(@RequestBody CollegeRequestDto collegeRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(collegeService.addCollegeDto(collegeRequestDto));
    }

    @PutMapping("/updateCollege")
    public Mono<CollegeResponseDto> updateCollegeDto(@RequestBody CollegeRequestDto collegeRequestDto) {
        return collegeService.updateCollegeDto(collegeRequestDto);
    }

    @DeleteMapping("/deleteCollege")
    public ResponseEntity<Mono<Void>> deleteCollegeDto(@RequestParam long collegeId) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(collegeService.deleteCollegeDto(collegeId));
    }
}
