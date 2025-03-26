package com.example.stream.spring.courses.reactive.example.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
    
    @NotBlank
    @Size(min = 3, max = 100)
    @Column("name")
    private String surname;

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
    @Column("freshman")
    private String freshman;
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
