package com.example.stream.spring.courses.reactive.example.controller;

import com.example.stream.spring.courses.reactive.example.model.request.DepartmentRequestDto;
import com.example.stream.spring.courses.reactive.example.model.response.DepartmentResponseDto;
import com.example.stream.spring.courses.reactive.example.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processEmptyResponse;
import static com.example.stream.spring.courses.reactive.example.utility.ProcessResponses.processTheResultFromService;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/getAllDepartment")
    public Flux<DepartmentResponseDto> getAllDepartment() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/getDepartmentById")
    public Mono<Optional<DepartmentResponseDto>> getDepartmentById(@RequestParam String departmentId) {
        return processTheResultFromService(departmentService.getDepartmentById(departmentId));
    }

    @PostMapping("/addDepartment")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Optional<DepartmentResponseDto>> addDepartment(@RequestBody DepartmentRequestDto departmentRequestDto) {
        return processTheResultFromService(departmentService.createDepartment(departmentRequestDto));
    }

    @PutMapping("/updateDepartment")
    public Mono<Optional<DepartmentResponseDto>> updateDepartment(@RequestParam String departmentId, @RequestBody DepartmentRequestDto requestDto) {
        return processTheResultFromService(departmentService.updateDepartment(departmentId, requestDto));
    }

    @DeleteMapping("/deleteDepartment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteDepartment(@RequestParam String departmentId) {
        return processEmptyResponse(departmentService.deleteDepartment(departmentId));
    }
}
