package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.CampusConverter;
import com.example.stream.spring.courses.reactive.example.entity.Campus;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.CampusNotFound;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.Success;
import com.example.stream.spring.courses.reactive.example.model.request.CampusRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CampusResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CampusRepository;
import com.example.stream.spring.courses.reactive.example.repository.UniversityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service class for managing campus entities in a reactive manner.
 * Provides methods for retrieving, creating, updating, and deleting campuses.
 */
@Service
public class CampusService {

    private final CampusRepository campusRepository;
    private final CampusConverter campusConverter;
    private final UniversityRepository universityRepository;

    /**
     * Constructs a {@code CampusService} with the specified repository and converter.
     *
     * @param campusRepository the repository for campus entities
     * @param campusConverter  the converter between entity and DTO
     */
    public CampusService(CampusRepository campusRepository, CampusConverter campusConverter, UniversityRepository universityRepository) {
        this.campusRepository = campusRepository;
        this.campusConverter = campusConverter;
        this.universityRepository = universityRepository;
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
    public Mono<Either<Error, CampusResponseDto>> getCampusById(String campusId) {
        return retrieveCampusById(campusId)
                .map(campusConverter::toDto);
    }

    /**
     * Adds a new campus based on the provided request data.
     *
     * @param campusRequestDto the data transfer object containing campus details
     * @return a {@link Mono} emitting the created {@link CampusResponseDto}
     */
    public Mono<Either<Error, CampusResponseDto>> addCampus(CampusRequestDto campusRequestDto) {
        return existUniversity(campusRequestDto.universityId())
                .filter(isPresent -> isPresent)
                .flatMap(isPresent -> campusRepository.save(campusConverter.toEntity(campusRequestDto))
                        .map(campus -> Either.right(campusConverter.toDto(campus))))
                .switchIfEmpty(Mono.just(Either.left()));
    }

    /**
     * Deletes a campus by its unique identifier.
     *
     * @param campusId the unique identifier of the campus to delete
     * @return a {@link Mono} that completes when the deletion is done
     */
    public Mono<Either<Error, Success>> deleteCampus(String campusId) {
        return campusRepository.deleteById(UUID.fromString(campusId));
    }

    private Mono<Either<Error, Boolean>> existUniversity(String universityId) {
        return universityRepository.existsById(UUID.fromString(universityId))
                .filter(Boolean::booleanValue)
                .map(Either::right)
                .switchIfEmpty(Mono.just(Either.left()));
    }

    private Mono<Either<Error, Campus>> retrieveCampusById(String campusId) {
        return campusRepository.findById(UUID.fromString(campusId))
                .<Either<Error, Campus>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new CampusNotFound())));
    }

    /**
     * Updates an existing campus with the provided request data.
     *
     * @param campusRequestDto the data transfer object containing updated campus details
     * @return a {@link Mono} emitting the updated {@link CampusResponseDto}
     */
    public Mono<Either<Error, CampusResponseDto>> updateCampus(String campusId, CampusRequestDto campusRequestDto) {
        return existUniversity(campusRequestDto.universityId())
                .filter(isPresent -> isPresent)
                .flatMap(isPresent -> retrieveCampusById(campusId)
                        .flatMap(campus -> campusRepository.save(updateCampus(campus, campusRequestDto))
                                .map(campusConverter::toDto))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The campus is not found"))))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The university is not found")));

    }

    private Campus updateCampus(Campus campus, CampusRequestDto campusRequestDto) {
        campus.setName(campusRequestDto.name());
        campus.setCity(campusRequestDto.city());
        campus.setAddress(campusRequestDto.address());
        campus.setCountry(campusRequestDto.country());
        campus.setUniversityId(UUID.fromString(campusRequestDto.universityId()));
        return campus;
    }
}
