package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.ClassroomConverter;
import com.example.stream.spring.courses.reactive.example.entity.Classroom;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.*;
import com.example.stream.spring.courses.reactive.example.model.request.ClassroomRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.ClassroomResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.ClassroomRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.example.stream.spring.courses.reactive.example.utility.UtilMono.createMonoWithError;

/**
 * {@code ClassroomService} encapsulates the core business logic for managing {@code Classroom} entities.
 * <p>
 * It interacts with the underlying {@link ClassroomRepository} to perform data operations in a non-blocking, reactive way.
 * The service also collaborates with {@link BuildingService} to ensure referential integrity, e.g., verifying building existence
 * before classroom creation or updates.
 * </p>
 *
 * <p>This service follows a functional error-handling strategy using {@code Either<Error, T>} to distinguish success and failure flows
 * without throwing exceptions. This design pattern offers:
 * <ul>
 *     <li>Improved control flow transparency</li>
 *     <li>Better testing and composability</li>
 *     <li>Elimination of unchecked exceptions</li>
 * </ul>
 * </p>
 *
 * <p>Each public method emits either a valid DTO or an error using {@link Mono} or {@link Flux}, allowing fine-grained
 * backpressure and non-blocking pipelines suitable for high-throughput applications.</p>
 */
@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassroomConverter converter;
    private final BuildingService buildingService;

    /**
     * Constructs a new {@code ClassroomService}.
     *
     * @param classroomRepository repository to interact with classroom persistence
     * @param converter           converter to map between entities and DTOs
     * @param buildingService     service to validate building existence
     */
    public ClassroomService(ClassroomRepository classroomRepository,
                            ClassroomConverter converter,
                            BuildingService buildingService) {
        this.classroomRepository = classroomRepository;
        this.converter = converter;
        this.buildingService = buildingService;
    }

    /**
     * Retrieves all classrooms from the database and maps them to response DTOs.
     *
     * <p>This is a read-only operation that streams classroom records reactively.</p>
     *
     * @return a {@link Flux} of {@link ClassroomResponseDto}, representing all stored classrooms
     */
    public Flux<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll()
                .map(converter::toDto);
    }

    /**
     * Internal helper to fetch a classroom by ID, returning a functional {@code Either<Error, Classroom>}.
     *
     * @param classroomId the UUID of the classroom as a string
     * @return a {@link Mono} emitting either the classroom or a {@code CampusNotFound} error
     */
    private Mono<Either<Error, Classroom>> retrieveClassroom(String classroomId) {
        return classroomRepository.findById(UUID.fromString(classroomId))
                .<Either<Error, Classroom>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new CampusNotFound())));
    }

    /**
     * Retrieves a classroom by its ID and maps it to a response DTO.
     *
     * <p>This method composes error and success types via {@code Either},
     * allowing the caller to handle the result functionally.</p>
     *
     * @param classroomId the classroom ID as string
     * @return a {@link Mono} of {@code Either<Error, ClassroomResponseDto>}
     */
    public Mono<Either<Error, ClassroomResponseDto>> getClassroomById(String classroomId) {
        return retrieveClassroom(classroomId)
                .map(errorClassroomEither -> errorClassroomEither.map(converter::toDto));
    }

    /**
     * Creates a new classroom if the associated building exists.
     *
     * <p>Validates the existence of the building referenced by the request.
     * If the building exists, a new classroom is persisted and returned.
     * Otherwise, returns an appropriate domain error.</p>
     *
     * @param classroomRequestDto the data for creating a classroom
     * @return a {@link Mono} containing either an error or the created classroom response DTO
     */
    public Mono<Either<Error, ClassroomResponseDto>> addClassroom(ClassroomRequestDto classroomRequestDto) {
        return buildingService.existBuildingById(classroomRequestDto.buildingId())
                .flatMap(errorBooleanEither -> errorBooleanEither.getRight()
                        .map(buildingExists -> {
                            Classroom classroom = converter.toEntity(classroomRequestDto);
                            return classroomRepository.save(classroom)
                                    .<Either<Error, ClassroomResponseDto>>map(saved -> Either.right(converter.toDto(saved)));
                        })
                        .orElse(createMonoWithError(errorBooleanEither)));
    }

    /**
     * Updates an existing classroom if both the building and classroom exist.
     *
     * <p>First ensures the target building exists. Then checks if the classroom exists.
     * If both checks pass, the classroom is updated with the new information from the request DTO.</p>
     *
     * @param classroomId         the ID of the classroom to update
     * @param classroomRequestDto the updated classroom data
     * @return a {@link Mono} emitting either the updated classroom or an error
     */
    public Mono<Either<Error, ClassroomResponseDto>> updateClassroom(String classroomId, ClassroomRequestDto classroomRequestDto) {
        return buildingService.existBuildingById(classroomRequestDto.buildingId())
                .flatMap(either -> either.getRight()
                        .map(buildingExists -> existClassroomById(classroomId)
                                .flatMap(errorBooleanEither -> errorBooleanEither.getRight()
                                        .map(classroomExists ->
                                                classroomRepository.save(converter.toEntity(classroomRequestDto))
                                                        .<Either<Error, ClassroomResponseDto>>map(updated -> Either.right(converter.toDto(updated))))
                                        .orElse(Mono.just(Either.left(new GenericError())))))
                        .orElse(createMonoWithError(either)));
    }

    /**
     * Checks whether a classroom with the given ID exists in the database.
     *
     * @param classroomId the UUID string of the classroom
     * @return a {@link Mono} containing {@code Either<Error, Boolean>} indicating existence
     */
    private Mono<Either<Error, Boolean>> existClassroomById(String classroomId) {
        return classroomRepository.existsById(UUID.fromString(classroomId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new ClassroomNotFound())));
    }

    /**
     * Deletes a classroom if it exists.
     *
     * <p>The operation first validates the classroom's presence.
     * If found, it deletes it and emits a {@link ClassroomDeleteOk} success signal.</p>
     *
     * @param classroomId the ID of the classroom to be deleted
     * @return a {@link Mono} emitting either a deletion success or an error
     */
    public Mono<Either<Error, Success>> deleteClassroom(String classroomId) {
        return existClassroomById(classroomId)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(exists ->
                                classroomRepository.deleteById(UUID.fromString(classroomId))
                                        .then(Mono.just(Either.right(new ClassroomDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }
}
