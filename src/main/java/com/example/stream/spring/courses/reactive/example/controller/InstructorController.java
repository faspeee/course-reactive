package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.InstructorRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.InstructorResponseDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    /**
     * Retrieves all students associated with a specific teacher.
     *
     * @param teacherId the ID of the teacher
     * @return a Flux containing StudentDTOs of the associated students
     */
    @Operation(
            summary = "Get Students by Teacher ID",
            description = "Retrieves all students associated with the specified teacher ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of students"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @GetMapping("/{teacherId}/students")
    public Flux<StudentResponseDto> getStudentsByTeacher(@PathVariable String teacherId) {
        return instructorService.getStudentsByTeacherId(teacherId);
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
    public Mono<InstructorResponseDto> addInstructor(@RequestBody InstructorRequestDto requestDto) {
        return instructorService.createInstructor(requestDto);
    }

    @PutMapping("/updateInstructor")
    public Mono<InstructorResponseDto> updateInstructor(@RequestParam String instructorId, @RequestBody InstructorRequestDto requestDto) {
        return instructorService.updateInstructor(instructorId, requestDto);
    }

    @DeleteMapping("/deleteInstructor")
    public Mono<Void> deleteInstructor(@RequestParam String instructorId) {
        return instructorService.deleteInstructor(instructorId);
    }
}
