package com.clicks.fulafiaresultcheckingverificationsystem.model.student;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResultCourse {

    @Id @GeneratedValue(strategy = AUTO)
    private Long id;

    @OneToOne
    private Course course;

    @Enumerated(STRING)
    private Semester currentSemester;

    private Integer currentLevel;

    private String currentSession;
}
