package com.example.stream.spring.courses.reactive.example.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("enrollment")
public class Enrollment {

    @Id
    private Long id;

    @Column("student_id")
    private Long studentId;

    @Column("course_id")
    private Long courseId;

    @Column("enrolled_at")
    private LocalDateTime enrolledAt;
}
