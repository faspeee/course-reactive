package com.example.stream.spring.courses.reactive.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("course_prerequisite")
public class CoursePrerequisite {

    @Id
    private UUID id;

    @Column("course_id")
    private Long courseId;

    @Column("prerequisite_id")
    private Long prerequisiteId;
}
