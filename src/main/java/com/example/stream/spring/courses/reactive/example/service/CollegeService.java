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

/**
 * Service class for managing college entities in a reactive manner.
 * Provides methods for retrieving, creating, updating, and deleting colleges.
 */
@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final CollegeConverter converter;
    private final UniversityRepository universityRepository;

    /**
     * Constructs a CollegeService with the specified repositories and converter.
     *
     * @param collegeRepository    the repository for college entities
     * @param converter            the converter between entity and DTO
     * @param universityRepository the repository for university entities
     */
    public CollegeService(CollegeRepository collegeRepository, CollegeConverter converter, UniversityRepository universityRepository) {
        this.collegeRepository = collegeRepository;
        this.converter = converter;
        this.universityRepository = universityRepository;
    }

    /**
     * Retrieves a college by its unique identifier.
     *
     * @param collegeId the unique identifier of the college
     * @return a {@link Mono} emitting the {@link CollegeResponseDto} if found, or empty if not found
     */
    public Mono<CollegeResponseDto> getCollege(long collegeId) {
        return collegeRepository.findById(collegeId)
                .map(converter::toDto);
    }

    /**
     * Retrieves all colleges.
     *
     * @return a {@link Flux} emitting all {@link CollegeResponseDto}s
     */
    public Flux<CollegeResponseDto> getAllCollege() {
        return collegeRepository.findAll()
                .map(converter::toDto);
    }

    /**
     * Adds a new college based on the provided request data.
     * Validates the existence of the associated university before creation.
     *
     * @param collegeRequestDto the data transfer object containing college details
     * @return a {@link Mono} emitting the created {@link CollegeResponseDto}
     * @throws ResponseStatusException if the associated university is not found
     */
    public Mono<CollegeResponseDto> addCollegeDto(CollegeRequestDto collegeRequestDto) {
        return universityRepository.findById(collegeRequestDto.universityId())
                .flatMap(university -> collegeRepository.save(converter.toEntity(collegeRequestDto))
                        .map(converter::toDto))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The university is not found")));
    }

    /**
     * Updates an existing college with the provided request data.
     *
     * @param collegeRequestDto the data transfer object containing updated college details
     * @return a {@link Mono} emitting the updated {@link CollegeResponseDto}
     */
    public Mono<CollegeResponseDto> updateCollegeDto(CollegeRequestDto collegeRequestDto) {
        return collegeRepository.save(converter.toEntity(collegeRequestDto))
                .map(converter::toDto);
    }

    /**
     * Deletes a college by its unique identifier.
     *
     * @param collegeId the unique identifier of the college to delete
     * @return a {@link Mono} that completes when the deletion is done
     */
    public Mono<Void> deleteCollegeDto(long collegeId) {
        return collegeRepository.deleteById(collegeId);
    }
}
