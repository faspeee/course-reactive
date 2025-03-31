package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.ClassroomConverter;
import com.example.stream.spring.courses.reactive.example.entity.Classroom;
import com.example.stream.spring.courses.reactive.example.model.request.ClassroomRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.ClassroomResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.BuildingRepository;
import com.example.stream.spring.courses.reactive.example.repository.ClassroomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<ClassroomResponseDto> getClassroomById(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .map(converter::toDto)
                .switchIfEmpty(Mono.empty()); // Returns empty Mono if not found
    }

    public Mono<ClassroomResponseDto> addClassroom(ClassroomRequestDto classroomRequestDto) {
        return buildingRepository.findById(classroomRequestDto.buildingId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campus not found")))
                .flatMap(building -> {
                    Classroom classroom = converter.toEntity(classroomRequestDto);
                    return classroomRepository.save(classroom)
                            .map(converter::toDto);
                });

    }

    public Mono<ClassroomResponseDto> updateClassroom(Long classroomId, ClassroomRequestDto classroomRequestDto) {
        return buildingRepository.findById(classroomRequestDto.buildingId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "classroom not found")))
                .flatMap(building ->
                        classroomRepository.findById(classroomId)
                                .flatMap(existingClassroom -> classroomRepository
                                        .save(converter.toEntity(classroomRequestDto)))
                                .map(converter::toDto)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "classroom not found")))// Returns empty Mono if not found
                );
    }

    public Mono<Void> deleteClassroom(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .switchIfEmpty(Mono.empty()) // Completes without emitting if not found
                .flatMap(classroomRepository::delete);
    }
}
