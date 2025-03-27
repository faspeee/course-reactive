package com.example.stream.spring.courses.reactive.example.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("building")
@Getter
@Setter
public class Building {

    @Id
    private Long id;

    private String name;
    private String code;

    @Column("campus_id")
    private Long campusId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    private Long version;

    @Transient
    private String identifier;

}
