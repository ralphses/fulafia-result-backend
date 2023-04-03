package com.clicks.fulafiaresultcheckingverificationsystem.model.student;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.CourseGrade;
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
@AllArgsConstructor
@NoArgsConstructor
public class StudentResult {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;
    private String currentSession;

    @Enumerated(EnumType.STRING)
    private Semester currentSemester;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private List<CourseGrade> courseGrades;

    private Integer currentLevel;
    private String currentRemarks;

    @Column(unique = true)
    private String resultCode;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private ResultAnalysis currentResultAnalysis;
}

