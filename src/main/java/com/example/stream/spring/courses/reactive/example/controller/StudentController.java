package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.StudentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/getAllStudents")
    public Flux<StudentResponseDto> findAllStudent() {
        return studentService.getAllStudents();
    }

    @GetMapping(value = "/getStudent", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<StudentResponseDto>> getStudent(
            @Parameter(description = "ID of the studentId to be retrieved") @RequestParam("studentId") String studentId) {
        return studentService.getStudentById(studentId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/addStudent", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<StudentResponseDto> addStudent(
            @Parameter(description = "Student details for the new studentId") @RequestBody StudentRequestDto studentRequestDto) {
        return studentService.createStudent(studentRequestDto);
    }

    @PutMapping(value = "/updateStudent", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<StudentResponseDto>> updateStudent(
            @Parameter(description = "Updated department details") @RequestBody StudentRequestDto studentRequestDto,
            @RequestParam String studentId) {
        return studentService.updateStudent(studentId, studentRequestDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/deleteStudent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteStudent(
            @Parameter(description = "ID of the studentId to be deleted") @RequestParam("studentId") String studentId) {
        return studentService.deleteStudent(studentId);
    }


    @GetMapping("/{courseId}/studentsByCourse")
    public Flux<StudentResponseDto> getStudentsByCourse(@PathVariable String courseId) {
        return studentService.getStudentsByCourseId(courseId);
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
    @GetMapping("/{teacherId}/studentsByTeacher")
    public Flux<StudentResponseDto> getStudentsByTeacher(@PathVariable String teacherId) {
        return studentService.getStudentsByTeacherId(teacherId);
    }
}
