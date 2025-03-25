package com.example.stream.spring.courses.reactive.example.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("instructor")
public class Instructor {

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

    @Column("department_id")
    private Long departmentId;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column("version")
    private Long version;
}
