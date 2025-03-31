package com.example.stream.spring.courses.reactive.example.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("college")
@Getter
@Setter
public class College {

    @Id
    private Long id;

    private String name;
    private String dean;

    @Column("university_id")
    private Long universityId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Transient
    private String identifier;
    private Long version;

}
