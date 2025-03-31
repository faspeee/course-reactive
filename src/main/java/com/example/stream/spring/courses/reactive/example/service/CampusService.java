package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.CampusConverter;
import com.example.stream.spring.courses.reactive.example.model.request.CampusRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CampusResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CampusRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service class for managing campus entities in a reactive manner.
 * Provides methods for retrieving, creating, updating, and deleting campuses.
 */
@Service
public class CampusService {

    private final CampusRepository campusRepository;
    private final CampusConverter campusConverter;

    /**
     * Constructs a {@code CampusService} with the specified repository and converter.
     *
     * @param campusRepository the repository for campus entities
     * @param campusConverter  the converter between entity and DTO
     */
    public CampusService(CampusRepository campusRepository, CampusConverter campusConverter) {
        this.campusRepository = campusRepository;
        this.campusConverter = campusConverter;
    }

    /**
     * Retrieves all campuses.
     *
     * @return a {@link Flux} emitting all {@link CampusResponseDto}s
     */
    public Flux<CampusResponseDto> getAllCampus() {
        return campusRepository.findAll()
                .map(campusConverter::toDto);
    }

    /**
     * Retrieves a campus by its unique identifier.
     *
     * @param campusId the unique identifier of the campus
     * @return a {@link Mono} emitting the {@link CampusResponseDto} if found, or empty if not found
     */
    public Mono<CampusResponseDto> getCampus(long campusId) {
        return campusRepository.findById(campusId)
                .map(campusConverter::toDto);
    }

    /**
     * Adds a new campus based on the provided request data.
     *
     * @param campusRequestDto the data transfer object containing campus details
     * @return a {@link Mono} emitting the created {@link CampusResponseDto}
     */
    public Mono<CampusResponseDto> addCampus(CampusRequestDto campusRequestDto) {
        return campusRepository.save(campusConverter.toEntity(campusRequestDto))
                .map(campusConverter::toDto);
    }

    /**
     * Deletes a campus by its unique identifier.
     *
     * @param campusId the unique identifier of the campus to delete
     * @return a {@link Mono} that completes when the deletion is done
     */
    public Mono<Void> deleteCampus(long campusId) {
        return campusRepository.deleteById(campusId);
    }

    /**
     * Updates an existing campus with the provided request data.
     *
     * @param campusRequestDto the data transfer object containing updated campus details
     * @return a {@link Mono} emitting the updated {@link CampusResponseDto}
     */
    public Mono<CampusResponseDto> updateCampus(CampusRequestDto campusRequestDto) {
        return campusRepository.save(campusConverter.toEntity(campusRequestDto))
                .map(campusConverter::toDto);
    }
}
