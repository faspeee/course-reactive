package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.CollegeConverter;
import com.example.stream.spring.courses.reactive.example.entity.College;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.*;
import com.example.stream.spring.courses.reactive.example.model.request.CollegeRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.CollegeResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.CollegeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.stream.spring.courses.reactive.example.utility.UtilMono.createMonoWithError;

/**
 * Service class for managing college entities in a reactive manner.
 * Provides methods for retrieving, creating, updating, and deleting colleges.
 */
@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final CollegeConverter converter;
    private final UniversityService universityService;

    /**
     * Constructs a CollegeService with the specified repositories and converter.
     *
     * @param collegeRepository the repository for college entities
     * @param converter         the converter between entity and DTO
     * @param universityService the service for university
     */
    public CollegeService(CollegeRepository collegeRepository, CollegeConverter converter, UniversityService universityService) {
        this.collegeRepository = collegeRepository;
        this.converter = converter;
        this.universityService = universityService;
    }

    private Mono<Either<Error, College>> retrieveCollegeById(String collegeId) {
        return collegeRepository.findById(UUID.fromString(collegeId))
                .<Either<Error, College>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new CollegeNotFound())));
    }

    /**
     * Retrieves a college by its unique identifier.
     *
     * @param collegeId the unique identifier of the college
     * @return a {@link Mono} emitting the {@link CollegeResponseDto} if found, or empty if not found
     */
    public Mono<Either<Error, CollegeResponseDto>> getCollege(String collegeId) {
        return retrieveCollegeById(collegeId)
                .map(errorCollegeEither -> errorCollegeEither.map(converter::toDto));
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
    public Mono<Either<Error, CollegeResponseDto>> addCollegeDto(CollegeRequestDto collegeRequestDto) {
        return universityService.existUniversityById(collegeRequestDto.universityId())
                .flatMap(either -> either.getRight()
                        .map(aBoolean -> collegeRepository.save(converter.toEntity(collegeRequestDto))
                                .<Either<Error, CollegeResponseDto>>map(college -> Either.right(converter.toDto(college))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Updates an existing college with the provided request data.
     *
     * @param collegeRequestDto the data transfer object containing updated college details
     * @return a {@link Mono} emitting the updated {@link CollegeResponseDto}
     */
    public Mono<Either<Error, CollegeResponseDto>> updateCollegeDto(String collegeId, CollegeRequestDto collegeRequestDto) {
        return universityService.existUniversityById(collegeRequestDto.universityId())
                .flatMap(either -> either.getRight()
                        .map(aBoolean -> existCollegeById(collegeId)
                                .flatMap(either1 -> either1.getRight()
                                        .map(aBoolean1 -> collegeRepository.save(converter.toEntity(collegeRequestDto))
                                                .<Either<Error, CollegeResponseDto>>map(college -> Either.right(converter.toDto(college))))
                                        .orElse(Mono.just(Either.left(new GenericError())))))
                        .orElse(createMonoWithError(either)));
    }

    public Mono<Either<Error, Boolean>> existCollegeById(String campusId) {
        return collegeRepository.existsById(UUID.fromString(campusId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new BuildingNotFound())));
    }

    /**
     * Deletes a college by its unique identifier.
     *
     * @param collegeId the unique identifier of the college to delete
     * @return a {@link Mono} that completes when the deletion is done
     */
    public Mono<Either<Error, Success>> deleteCollegeDto(String collegeId) {
        return existCollegeById(collegeId)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(building ->
                                collegeRepository.deleteById(UUID.fromString(collegeId))
                                        .then(Mono.just(Either.right(new CourseDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }
}
