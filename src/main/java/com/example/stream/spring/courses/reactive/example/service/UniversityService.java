package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.UniversityConverter;
import com.example.stream.spring.courses.reactive.example.entity.University;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.*;
import com.example.stream.spring.courses.reactive.example.model.request.UniversityRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.UniversityResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.UniversityRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.stream.spring.courses.reactive.example.utility.UtilMono.createMonoWithError;

/**
 * {@code UniversityService} encapsulates business logic for managing {@link University} entities.
 * <p>
 * This service exposes operations such as creating, updating, retrieving, and deleting universities.
 * </p>
 * <p>
 * All interactions are reactive, leveraging {@link Mono} and {@link Flux} from Project Reactor for asynchronous,
 * non-blocking flows. Error handling is functional, using {@code Either<Error, T>} to represent success or failure.
 * </p>
 */
@Service
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityConverter universityConverter;

    /**
     * Constructs a {@code UniversityService} with its required dependencies.
     *
     * @param universityRepository repository interface for accessing {@link University} persistence
     * @param universityConverter  converter for transforming between entity and DTO representations
     */
    public UniversityService(UniversityRepository universityRepository, UniversityConverter universityConverter) {
        this.universityRepository = universityRepository;
        this.universityConverter = universityConverter;
    }

    /**
     * Retrieves all universities.
     *
     * @return a {@link Flux} of {@link UniversityResponseDto}
     */
    public Flux<UniversityResponseDto> findAllUniversity() {
        return universityRepository.findAll()
                .map(universityConverter::toDto);
    }

    /**
     * Retrieves a university by its UUID string.
     *
     * @param universityId the UUID string of the university
     * @return a {@link Mono} with {@code Either<Error, UniversityResponseDto>}
     */
    public Mono<Either<Error, UniversityResponseDto>> findUniversityById(String universityId) {
        return getUniversityById(UUID.fromString(universityId))
                .map(errorUniversityEither -> errorUniversityEither.map(universityConverter::toDto));
    }

    /**
     * Creates a new university from the provided DTO.
     *
     * @param universityRequestDto the input DTO representing the university
     * @return a {@link Mono} with {@code Either<Error, UniversityResponseDto>}
     */
    public Mono<Either<Error, UniversityResponseDto>> createUniversity(UniversityRequestDto universityRequestDto) {
        return universityRepository.save(universityConverter.toEntity(universityRequestDto))
                .<Either<Error, UniversityResponseDto>>map(university -> Either.right(universityConverter.toDto(university)))
                .switchIfEmpty(Mono.just(Either.left(new GenericError())));
    }

    /**
     * Updates a university entity's properties using data from the request DTO.
     *
     * @param university           the university entity to update
     * @param universityRequestDto the DTO containing the new values
     * @return the updated {@link University} entity
     */
    private University updateUniversity(University university, UniversityRequestDto universityRequestDto) {
        university.setAccreditation(universityRequestDto.accreditation());
        university.setContactEmail(universityRequestDto.contactEmail());
        university.setCountry(universityRequestDto.country());
        university.setCity(universityRequestDto.city());
        university.setColors(universityRequestDto.colors());
        university.setCampusArea(universityRequestDto.campusArea());
        university.setEstablished(universityRequestDto.established());
        university.setMotto(universityRequestDto.motto());
        university.setPhoneNumber(universityRequestDto.phoneNumber());
        university.setMascot(universityRequestDto.mascot());
        university.setRanking(universityRequestDto.ranking());
        university.setInternational(universityRequestDto.international());
        university.setName(universityRequestDto.name());
        university.setNumFaculties(universityRequestDto.numFaculties());
        university.setNumPrograms(universityRequestDto.numPrograms());
        university.setPresident(universityRequestDto.president());
        university.setStudentCount(universityRequestDto.studentCount());
        university.setWebsite(universityRequestDto.website());
        return university;
    }

    /**
     * Retrieves a university entity by ID and wraps it in an {@code Either}.
     *
     * @param universityId UUID of the university
     * @return {@link Mono} with {@code Either<Error, University>}
     */
    private Mono<Either<Error, University>> getUniversityById(UUID universityId) {
        return universityRepository.findById(universityId)
                .<Either<Error, University>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new UniversityNotFound())));
    }

    /**
     * Updates a university by ID using new data from a DTO.
     *
     * @param universityId         the ID of the university to update
     * @param universityRequestDto the new university data
     * @return {@link Mono} with {@code Either<Error, UniversityResponseDto>}
     */
    public Mono<Either<Error, UniversityResponseDto>> updateUniversity(String universityId, UniversityRequestDto universityRequestDto) {
        return getUniversityById(UUID.fromString(universityId))
                .flatMap(either -> either.getRight()
                        .map(university -> universityRepository.save(updateUniversity(university, universityRequestDto))
                                .<Either<Error, UniversityResponseDto>>map(updated -> Either.right(universityConverter.toDto(updated))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Checks whether a university exists by ID.
     *
     * @param universityId the UUID string of the university
     * @return {@code Mono<Either<Error, Boolean>>}, right(true) if exists, else left(error)
     */
    public Mono<Either<Error, Boolean>> existUniversityById(String universityId) {
        return universityRepository.existsById(UUID.fromString(universityId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new UniversityNotFound())));
    }

    /**
     * Deletes a university by ID if it exists.
     *
     * @param universityId the ID of the university to delete
     * @return {@link Mono} with {@code Either<Error, Success>}
     */
    public Mono<Either<Error, Success>> deleteUniversity(String universityId) {
        return existUniversityById(universityId)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(exists ->
                                universityRepository.deleteById(UUID.fromString(universityId))
                                        .then(Mono.just(Either.right(new UniversityDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }

}
