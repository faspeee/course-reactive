package com.example.stream.spring.courses.reactive.example.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("classroom")
@Getter
@Setter
public class Classroom {

    @Id
    private Long id;

    @Column("building_id")
    private Long buildingId;

    @Column("room_number")
    private String roomNumber;

    private Integer capacity;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    private Long version;
    @Transient
    private String identifier;
}
