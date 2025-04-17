package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.ClassroomConverter;
import com.example.stream.spring.courses.reactive.example.entity.Classroom;
import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.Success;
import com.example.stream.spring.courses.reactive.example.model.request.ClassroomRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.ClassroomResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.BuildingRepository;
import com.example.stream.spring.courses.reactive.example.repository.ClassroomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassroomConverter converter;
    private final BuildingRepository buildingRepository;

    public ClassroomService(ClassroomRepository classroomRepository, ClassroomConverter converter, BuildingRepository buildingRepository) {
        this.classroomRepository = classroomRepository;
        this.converter = converter;
        this.buildingRepository = buildingRepository;
    }

    public Flux<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll()
                .map(converter::toDto);
    }

    public Mono<Either<Error, ClassroomResponseDto>> getClassroomById(String classroomId) {
        return classroomRepository.findById(UUID.fromString(classroomId))
                .map(converter::toDto)
                .switchIfEmpty(Mono.empty()); // Returns empty Mono if not found
    }

    public Mono<Either<Error, ClassroomResponseDto>> addClassroom(ClassroomRequestDto classroomRequestDto) {
        return buildingRepository.findById(UUID.fromString(classroomRequestDto.buildingId()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campus not found")))
                .flatMap(building -> {
                    Classroom classroom = converter.toEntity(classroomRequestDto);
                    return classroomRepository.save(classroom)
                            .map(converter::toDto);
                });

    }

    public Mono<Either<Error, ClassroomResponseDto>> updateClassroom(String classroomId, ClassroomRequestDto classroomRequestDto) {
        return buildingRepository.findById(UUID.fromString(classroomRequestDto.buildingId()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "classroom not found")))
                .flatMap(building ->
                        classroomRepository.findById(UUID.fromString(classroomId))
                                .flatMap(existingClassroom -> classroomRepository
                                        .save(converter.toEntity(classroomRequestDto)))
                                .map(converter::toDto)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "classroom not found")))// Returns empty Mono if not found
                );
    }

    public Mono<Either<Error, Success>> deleteClassroom(String classroomId) {
        return classroomRepository.findById(UUID.fromString(classroomId))
                .switchIfEmpty(Mono.empty()) // Completes without emitting if not found
                .flatMap(classroomRepository::delete);
    }
}
