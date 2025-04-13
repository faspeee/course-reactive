package com.example.stream.spring.courses.reactive.example.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("university")
@Getter
@Setter
public class University {

    @Id
    @Column("id")
    private UUID id;

    @Column("name")
    private String name;

    @Column("location")
    private String location;

    @Column("established")
    private LocalDate established;

    @Column("accreditation")
    private String accreditation;

    @Column("president")
    private String president;

    @Column("student_count")
    private Integer studentCount;

    @Column("website")
    private String website;

    @Column("contact_email")
    private String contactEmail;

    @Column("phone_number")
    private String phoneNumber;

    @Column("motto")
    private String motto;

    @Column("colors")
    private String colors;

    @Column("mascot")
    private String mascot;

    @Column("campus_area")
    private Double campusArea;

    @Column("num_faculties")
    private Integer numFaculties;

    @Column("num_programs")
    private Integer numPrograms;

    @Column("international")
    private Boolean international;

    @Column("ranking")
    private Integer ranking;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("version")
    private Long version;

    private String country;
    private String city;
}
