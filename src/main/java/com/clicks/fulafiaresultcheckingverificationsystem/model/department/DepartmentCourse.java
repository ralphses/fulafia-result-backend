package com.clicks.fulafiaresultcheckingverificationsystem.model.department;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.CourseType;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DepartmentCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Course course;
    private Integer level;

    @Enumerated(EnumType.STRING)
    private CourseType type;
}
