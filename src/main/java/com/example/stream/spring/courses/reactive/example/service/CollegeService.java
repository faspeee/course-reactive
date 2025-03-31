package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.CollegeConverter;
import com.example.stream.spring.courses.reactive.example.model.request.CollegeRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CollegeResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CollegeRepository;
import com.example.stream.spring.courses.reactive.example.repository.UniversityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CollegeService {
    private final CollegeRepository collegeRepository;
    private final CollegeConverter converter;
    private final UniversityRepository universityRepository;

    public CollegeService(CollegeRepository collegeRepository, CollegeConverter converter, UniversityRepository universityRepository) {
        this.collegeRepository = collegeRepository;
        this.converter = converter;
        this.universityRepository = universityRepository;
    }

    public Mono<CollegeResponseDto> getCollege(long collegeId) {
        return collegeRepository.findById(collegeId)
                .map(converter::toDto);
    }

    public Flux<CollegeResponseDto> getAllCollege() {
        return collegeRepository.findAll()
                .map(converter::toDto);
    }

    public Mono<CollegeResponseDto> addCollegeDto(CollegeRequestDto collegeRequestDto) {
        return universityRepository.findById(collegeRequestDto.universityId())
                .flatMap(value -> collegeRepository.save(converter.toEntity(collegeRequestDto))
                        .map(converter::toDto))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "THe university is not found")));
    }

    public Mono<CollegeResponseDto> updateCollegeDto(CollegeRequestDto collegeRequestDto) {
        return collegeRepository.save(converter.toEntity(collegeRequestDto))
                .map(converter::toDto);
    }

    public Mono<Void> deleteCollegeDto(long collegeId) {
        return collegeRepository.deleteById(collegeId);
    }
}
