package com.clicks.fulafiaresultcheckingverificationsystem.model.student;

import com.clicks.fulafiaresultcheckingverificationsystem.model.department.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String matric;

    @Column(unique = true)
    private String email;

    @Column(length = 6)
    private String passcode;

    private Integer currentLevel;

    @ManyToMany()
    @JoinTable(name = "course_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<StudentRegisteredCourse> courses;

    @OneToOne(fetch = FetchType.EAGER)
    private StudentResult studentResult;

    @OneToOne(fetch = FetchType.EAGER)
    private Department department;
}
