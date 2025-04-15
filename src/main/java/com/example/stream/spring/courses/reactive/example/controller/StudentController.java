package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.StudentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.StudentResponseDto;
import com.example.stream.spring.courses.reactive.example.service.StudentService;
import io.swagger.v3.oas.annotations.Parameter;
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


}
