package com.example.stream.spring.courses.reactive.example.service;

public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public Flux<ClassroomResponseDto> getAllClassrooms() {
        return classroomRepository.findAll()
                .map(this::mapToResponseDto);
    }

    public Mono<ClassroomResponseDto> getClassroomById(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .map(this::mapToResponseDto)
                .switchIfEmpty(Mono.empty()); // Returns empty Mono if not found
    }

    public Mono<ClassroomResponseDto> addClassroom(ClassroomRequestDto classroomRequestDto) {
        Classroom classroom = mapToEntity(classroomRequestDto);
        return classroomRepository.save(classroom)
                .map(this::mapToResponseDto);
    }

    public Mono<ClassroomResponseDto> updateClassroom(Long classroomId, ClassroomRequestDto classroomRequestDto) {
        return classroomRepository.findById(classroomId)
                .flatMap(existingClassroom -> {
                    updateEntity(existingClassroom, classroomRequestDto);
                    return classroomRepository.save(existingClassroom);
                })
                .map(this::mapToResponseDto)
                .switchIfEmpty(Mono.empty()); // Returns empty Mono if not found
    }

    public Mono<Void> deleteClassroom(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .flatMap(existingClassroom ->
                        classroomRepository.delete(existingClassroom))
                .switchIfEmpty(Mono.empty()); // Completes without emitting if not found
    }
}
