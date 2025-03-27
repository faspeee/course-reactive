package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.CampusConverter;
import com.example.stream.spring.courses.reactive.example.model.request.CampusRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CampusResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CampusRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CampusService {
    private final CampusRepository campusRepository;
    private final CampusConverter campusConverter;

    public CampusService(CampusRepository campusRepository, CampusConverter campusConverter) {
        this.campusRepository = campusRepository;
        this.campusConverter = campusConverter;
    }

    public Flux<CampusResponseDto> getAllCampus() {
        return campusRepository.findAll()
                .map(campusConverter::toDto);
    }

    public Mono<CampusResponseDto> getCampus(long campusId) {
        return campusRepository.findById(campusId)
                .map(campusConverter::toDto);
    }

    public Mono<CampusResponseDto> addCampus(CampusRequestDto campusRequestDto) {
        return campusRepository.save(campusConverter.toEntity(campusRequestDto))
                .map(campusConverter::toDto);
    }

    public Mono<Void> deleteCampus(long campusId) {
        return campusRepository.deleteById(campusId);
    }

    public Mono<CampusResponseDto> updateCampus(CampusRequestDto campusRequestDto) {
        return campusRepository.save(campusConverter.toEntity(campusRequestDto))
                .map(campusConverter::toDto);
    }
}
