package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.model.request.CollegeRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CollegeResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CollegeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CollegeService {
    private final CollegeRepository collegeRepository;

    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    public Mono<CollegeResponseDto> getCollege(long collegeId) {
        return collegeRepository.findById(collegeId);
    }

    public Flux<CollegeResponseDto> getAllCollege() {
    }

    public Mono<CollegeResponseDto> addCollegeDto(CollegeRequestDto collegeRequestDto) {
    }

    public Mono<CollegeResponseDto> updateCollegeDto(CollegeRequestDto collegeRequestDto) {
    }

    public Mono<CollegeResponseDto> deleteCollegeDto(long collegeId) {
    }
}
