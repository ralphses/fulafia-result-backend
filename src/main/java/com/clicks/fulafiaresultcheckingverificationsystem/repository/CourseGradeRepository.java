package com.clicks.fulafiaresultcheckingverificationsystem.repository;

import com.clicks.fulafiaresultcheckingverificationsystem.model.course.CourseGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseGradeRepository extends JpaRepository<CourseGrade, Long> {
}
