package com.example.stream.spring.courses.reactive.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("instructor_department")
public class InstructorDepartment {

    @Id
    private Long id;

    @Column("instructor_id")
    private Long instructorId;

    @Column("department_id")
    private Long departmentId;

}
