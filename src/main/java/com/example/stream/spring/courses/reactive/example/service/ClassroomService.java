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

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassroomConverter converter;
    private final BuildingService buildingService;

    public ClassroomService(ClassroomRepository classroomRepository, ClassroomConverter converter, BuildingService buildingService) {
        this.classroomRepository = classroomRepository;
        this.converter = converter;
        this.buildingService = buildingService;
    }

    public Flux<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll()
                .map(converter::toDto);
    }

    private Mono<Either<Error, Classroom>> retrieveClassroom(String classroomId) {
        return classroomRepository.findById(UUID.fromString(classroomId))
                .<Either<Error, Classroom>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new CampusNotFound())));
    }

    public Mono<Either<Error, ClassroomResponseDto>> getClassroomById(String classroomId) {
        return retrieveClassroom(classroomId)
                .map(errorClassroomEither -> errorClassroomEither.map(converter::toDto));
    }

    public Mono<Either<Error, ClassroomResponseDto>> addClassroom(ClassroomRequestDto classroomRequestDto) {
        return buildingService.existBuildingById(classroomRequestDto.buildingId())
                .flatMap(errorBooleanEither -> errorBooleanEither.getRight()
                        .map(aBoolean -> {
                            Classroom classroom = converter.toEntity(classroomRequestDto);
                            return classroomRepository.save(classroom)
                                    .<Either<Error, ClassroomResponseDto>>map(classroom1 -> Either.right(converter.toDto(classroom1)));
                        })
                        .orElse(createMonoWithError(errorBooleanEither)));

    }

    public Mono<Either<Error, ClassroomResponseDto>> updateClassroom(String classroomId, ClassroomRequestDto classroomRequestDto) {
        return buildingService.existBuildingById(classroomRequestDto.buildingId())
                .flatMap(either -> either.getRight()
                        .map(result1 -> existClassroomById(classroomId)
                                .flatMap(errorBooleanEither -> errorBooleanEither.getRight()
                                        .map(result -> classroomRepository
                                                .save(converter.toEntity(classroomRequestDto))
                                                .<Either<Error, ClassroomResponseDto>>map(classroom -> Either.right(converter.toDto(classroom))))
                                        .orElse(Mono.just(Either.left(new GenericError())))))
                        .orElse(createMonoWithError(either)));
    }

    private Mono<Either<Error, Boolean>> existClassroomById(String classroomId) {
        return classroomRepository.existsById(UUID.fromString(classroomId))
                .filter(Boolean::booleanValue)
                .<Either<Error, Boolean>>map(Either::right)
                .switchIfEmpty(Mono.just(Either.left(new ClassroomNotFound())));
    }

    public Mono<Either<Error, Success>> deleteClassroom(String classroomId) {
        return existClassroomById(classroomId)
                .flatMap(either -> either.getRight()
                        .<Mono<Either<Error, Success>>>map(building ->
                                classroomRepository.deleteById(UUID.fromString(classroomId))
                                        .then(Mono.just(Either.right(new ClassroomDeleteOk()))))
                        .orElse(createMonoWithError(either)));
    }
}
