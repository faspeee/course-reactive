package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface Success permits BuildingSuccess, CampusSuccess, ClassroomSuccess, CollegeSuccess, CourseSuccess, DepartmentSuccess, InstructorSuccess, StudentSuccess, UniversitySuccess {
}
