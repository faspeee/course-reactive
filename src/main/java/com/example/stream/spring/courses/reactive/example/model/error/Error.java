package com.example.stream.spring.courses.reactive.example.model.error;

public sealed interface Error permits BuildingError, CampusError, ClassroomError, CollegeError, CourseError, DepartmentError, GenericError, InstructorError, StudentError, UniversityError {
}
