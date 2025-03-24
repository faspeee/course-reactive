package com.example.stream.spring.courses.reactive.example.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table(name = "course")
@Setter
@Getter
public class Course {
  @Id
  private Long id;
  @Column(value = "courseName")
  private String courseName;
  @Column(value = "courseCode")
  private String courseCode;
  @Column(value = "startDate")
  private LocalDate startDate;
  @Column(value = "endDate")
  private LocalDate endDate;
  @Column(value = "creditHours")
  private int creditHours;
}

