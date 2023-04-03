package com.clicks.fulafiaresultcheckingverificationsystem.model.student;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.CourseStatus;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.Course;
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
public class StudentRegisteredCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Course course;

    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    @Enumerated(EnumType.STRING)
    private Semester currentSemester;

    private Integer currentLevel;

    private String currentSession;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;
}
