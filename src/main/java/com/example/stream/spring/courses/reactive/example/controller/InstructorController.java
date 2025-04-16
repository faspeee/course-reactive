package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.InstructorRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.InstructorResponseDto;
import com.example.stream.spring.courses.reactive.example.service.InstructorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing teacher-related operations.
 */
@RestController
@RequestMapping("/teacher")
@Tag(name = "Teacher Management", description = "Endpoints for managing teachers and their associated students")
public class InstructorController {

    private final InstructorService instructorService;

    /**
     * Constructs a TeacherController with the specified TeacherService.
     *
     * @param instructorService the service to handle teacher-related operations
     */
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/getAllInstructors")
    public Flux<InstructorResponseDto> getAllInstructors() {
        return instructorService.getAllInstructor();
    }

    @GetMapping("/getInstructorById")
    public Mono<InstructorResponseDto> getInstructorById(@RequestParam String instructorId) {
        return instructorService.getInstructorById(instructorId);
    }

    @PostMapping("/addInstructor")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<InstructorResponseDto> addInstructor(@Valid @RequestBody InstructorRequestDto requestDto) {
        return instructorService.createInstructor(requestDto);
    }

    @PutMapping("/updateInstructor")
    public Mono<InstructorResponseDto> updateInstructor(@RequestParam String instructorId, @Valid @RequestBody InstructorRequestDto requestDto) {
        return instructorService.updateInstructor(instructorId, requestDto);
    }

    @DeleteMapping("/deleteInstructor")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteInstructor(@RequestParam String instructorId) {
        return instructorService.deleteInstructor(instructorId);
    }
}
