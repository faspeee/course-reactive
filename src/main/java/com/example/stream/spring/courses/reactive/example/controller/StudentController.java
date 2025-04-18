package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.StudentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processEmptyResponse;
import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processTheResultFromService;

@RestController
@RequestMapping("/student")
@Tag(name = "Student Management", description = "Endpoints for managing students and their relationships with courses and teachers")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
            summary = "Get All Students",
            description = "Retrieves a list of all students in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of students")
            }
    )
    @GetMapping("/getAllStudents")
    public Flux<StudentResponseDto> findAllStudent() {
        return studentService.getAllStudents();
    }

    @Operation(
            summary = "Get Student by ID",
            description = "Retrieves a student using the provided student ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student found"),
                    @ApiResponse(responseCode = "404", description = "Student not found")
            }
    )
    @GetMapping(value = "/getStudent", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Optional<StudentResponseDto>> getStudent(
            @Parameter(description = "ID of the student to be retrieved") @RequestParam("studentId") String studentId) {
        return processTheResultFromService(studentService.getStudentById(studentId));
    }

    @Operation(
            summary = "Create Student",
            description = "Adds a new student to the system using the provided student details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Student successfully created")
            }
    )
    @PostMapping(value = "/addStudent", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Optional<StudentResponseDto>> addStudent(
            @Parameter(description = "Student details for the new student") @RequestBody StudentRequestDto studentRequestDto) {
        return processTheResultFromService(studentService.createStudent(studentRequestDto));
    }

    @Operation(
            summary = "Update Student",
            description = "Updates an existing student's information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Student not found")
            }
    )
    @PutMapping(value = "/updateStudent", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Optional<StudentResponseDto>> updateStudent(
            @Parameter(description = "Updated student details") @RequestBody StudentRequestDto studentRequestDto,
            @Parameter(description = "ID of the student to update") @RequestParam String studentId) {
        return processTheResultFromService(studentService.updateStudent(studentId, studentRequestDto));
    }

    @Operation(
            summary = "Delete Student",
            description = "Deletes a student from the system based on the provided ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Student successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "student not found")
            }
    )
    @DeleteMapping("/deleteStudent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteStudent(
            @Parameter(description = "ID of the student to be deleted") @RequestParam("studentId") String studentId) {
        return processEmptyResponse(studentService.deleteStudent(studentId));

    }

    @Operation(
            summary = "Get Students by Course ID",
            description = "Fetches all students who are enrolled in the course specified by courseId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of enrolled students returned"),
                    @ApiResponse(responseCode = "404", description = "Course not found")
            }
    )
    @GetMapping("/{courseId}/studentsByCourse")
    public Flux<StudentResponseDto> getStudentsByCourse(
            @Parameter(description = "Course ID to fetch students for") @PathVariable String courseId) {
        return studentService.getStudentsByCourseId(courseId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The course is not found")));
    }

    @Operation(
            summary = "Get Students by Teacher ID",
            description = "Retrieves all students associated with the specified teacher ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of students"),
                    @ApiResponse(responseCode = "404", description = "Teacher not found")
            }
    )
    @GetMapping("/{teacherId}/studentsByTeacher")
    public Flux<StudentResponseDto> getStudentsByTeacher(
            @Parameter(description = "Teacher ID to retrieve students for") @PathVariable String teacherId) {
        return studentService.getStudentsByTeacherId(teacherId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "The teacher is not found")));
    }
}

