package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface Success permits BuildingSuccess, CampusSuccess, ClassroomSuccess, CollegeSuccess, DepartmentSuccess, InstructorSuccess, StudentSuccess, UniversitySuccess {
}
