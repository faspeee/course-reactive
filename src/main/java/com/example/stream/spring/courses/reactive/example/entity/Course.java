package com.example.stream.spring.courses.reactive.example.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("course")
public class Course {

    @Id
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column("course_name")
    private String courseName;

    @NotBlank
    @Size(max = 50)
    @Column("course_code")
    private String courseCode;

    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private LocalDate endDate;

    @Column("credit_hours")
    private int creditHours;

    @Size(max = 500)
    @Column("description")
    private String description;

    @Column("instructor_id")
    private Long instructorId;

    @Column("is_active")
    private boolean isActive = true;

    @NotNull
    @Column("department_id")
    private Long departmentId;

    @Transient
    private List<Long> studentIds;  // Loaded separately in service

    @Transient
    private List<Long> prerequisiteIds; // Loaded separately in service

    @Transient
    private String identifier;
}


