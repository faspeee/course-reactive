package com.example.stream.spring.courses.reactive.example.service;

import com.example.stream.spring.courses.reactive.example.converter.ClassroomConverter;
import com.example.stream.spring.courses.reactive.example.entity.Classroom;
import com.example.stream.spring.courses.reactive.example.model.request.ClassroomRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.ClassroomResponseDto;
import com.example.stream.spring.courses.reactive.example.repository.ClassroomRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassroomConverter converter;

    public ClassroomService(ClassroomRepository classroomRepository, ClassroomConverter converter) {
        this.classroomRepository = classroomRepository;
        this.converter = converter;
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
        Classroom classroom = converter.toEntity(classroomRequestDto);
        return classroomRepository.save(classroom)
                .map(converter::toDto);
    }

    public Mono<ClassroomResponseDto> updateClassroom(Long classroomId, ClassroomRequestDto classroomRequestDto) {
        return classroomRepository.findById(classroomId)
                .flatMap(existingClassroom -> classroomRepository
                        .save(converter.toEntity(classroomRequestDto)))
                .map(converter::toDto)
                .switchIfEmpty(Mono.empty()); // Returns empty Mono if not found
    }

    public Mono<Void> deleteClassroom(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .flatMap(existingClassroom ->
                        classroomRepository.delete(existingClassroom))
                .switchIfEmpty(Mono.empty()); // Completes without emitting if not found
    }
}
