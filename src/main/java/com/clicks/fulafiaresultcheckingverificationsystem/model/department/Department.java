package com.clicks.fulafiaresultcheckingverificationsystem.model.department;

import com.clicks.fulafiaresultcheckingverificationsystem.model.student.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    List<DepartmentCourse> courses;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    List<Student> students;
}
