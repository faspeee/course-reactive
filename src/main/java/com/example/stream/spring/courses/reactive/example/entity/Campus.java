package com.example.stream.spring.courses.reactive.example.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("campus")
@Getter
@Setter
public class Campus {

    @Id
    private UUID id;

    private String name;
    private String address;

    @Column("university_id")
    private Long universityId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Transient
    private LocalDate startDate;
    @Transient
    private LocalDate endDate;
    private String country;
    private String city;
    private Long version;
}
