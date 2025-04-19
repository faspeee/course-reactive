package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.UniversityRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.UniversityResponseDto;
import com.example.stream.spring.courses.reactive.example.service.UniversityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/university")
public class UniversityController {
    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping("/getAllUniversities")
    public Flux<UniversityResponseDto> getAllUniversities() {
        return universityService.findAllUniversity();
    }

    @GetMapping("/getUniversityById")
    public Mono<UniversityResponseDto> getUniversityById(@RequestParam String universityId) {
        return universityService.findUniversityById(universityId);
    }

    @PostMapping("/addUniversity")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UniversityResponseDto> addUniversity(@Valid @RequestBody UniversityRequestDto universityRequestDto) {
        return universityService.createUniversity(universityRequestDto);
    }

    @PutMapping("/updateUniversity")
    public Mono<UniversityResponseDto> updateUniversity(@RequestParam String universityId, @Valid @RequestBody UniversityRequestDto universityRequestDto) {
        return universityService.updateUniversity(universityId, universityRequestDto);
    }

    @DeleteMapping("/deleteUniversity")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUniversity(@RequestParam String universityId) {
        return universityService.deleteUniversity(universityId);
    }
}
