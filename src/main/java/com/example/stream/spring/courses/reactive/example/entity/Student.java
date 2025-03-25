package com.example.stream.spring.courses.reactive.example.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("student")
public class Student {

    @Id
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column("name")
    private String name;

    @Email
    @NotBlank
    @Column("email")
    private String email;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column("version")
    private Long version;

    @Transient
    private List<Long> courseIds = new ArrayList<>(); // Loaded separately in service

    public void enrollInCourse(Long courseId) {
        if (!this.courseIds.contains(courseId)) {
            this.courseIds.add(courseId);
        }
    }
}
