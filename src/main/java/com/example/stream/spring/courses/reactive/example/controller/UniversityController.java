package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.UniversityRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.UniversityResponseDto;
import com.example.stream.spring.courses.reactive.example.service.UniversityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processEmptyResponse;
import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processTheResultFromService;

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
    public Mono<Optional<UniversityResponseDto>> getUniversityById(@RequestParam String universityId) {
        return processTheResultFromService(universityService.findUniversityById(universityId));
    }

    @PostMapping("/addUniversity")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Optional<UniversityResponseDto>> addUniversity(@Valid @RequestBody UniversityRequestDto universityRequestDto) {
        return processTheResultFromService(universityService.createUniversity(universityRequestDto));
    }

    @PutMapping("/updateUniversity")
    public Mono<Optional<UniversityResponseDto>> updateUniversity(@RequestParam String universityId, @Valid @RequestBody UniversityRequestDto universityRequestDto) {
        return processTheResultFromService(universityService.updateUniversity(universityId, universityRequestDto));
    }

    @DeleteMapping("/deleteUniversity")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUniversity(@RequestParam String universityId) {
        return processEmptyResponse(universityService.deleteUniversity(universityId));
    }
}
