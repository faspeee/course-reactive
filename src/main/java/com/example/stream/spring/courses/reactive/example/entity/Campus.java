package com.example.stream.spring.courses.reactive.example.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("campus")
@Getter
@Setter
public class Campus {

    @Id
    private Long id;

    private String name;
    private String address;

    @Column("university_id")
    private Long universityId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    private String country;
    private String city;
    private Long version;
}
