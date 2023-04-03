package com.clicks.fulafiaresultcheckingverificationsystem.model.course;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.Grade;
import com.clicks.fulafiaresultcheckingverificationsystem.model.student.StudentResultCourse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.AUTO;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CourseGrade{

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private StudentResultCourse course;

    @Enumerated(value = STRING)
    private Grade grade;

    private Integer level;


}
