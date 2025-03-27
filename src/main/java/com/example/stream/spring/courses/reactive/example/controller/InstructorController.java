package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * REST controller for managing teacher-related operations.
 */
@RestController
@RequestMapping("/teachers")
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
    public Flux<StudentResponseDto> getStudentsByTeacher(@PathVariable Long teacherId) {
        return instructorService.getStudentsByTeacherId(teacherId);
    }
}
