package com.example.stream.spring.courses.reactive.example.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("course_prerequisite")
public class CoursePrerequisite {

    @Id
    private Long id;

    @Column("course_id")
    private Long courseId;

    @Column("prerequisite_id")
    private Long prerequisiteId;
}
