package com.clicks.fulafiaresultcheckingverificationsystem.repository;

import com.clicks.fulafiaresultcheckingverificationsystem.model.course.CourseGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseGradeRepository extends JpaRepository<CourseGrade, Long> {

    @Query(value = "SELECT course FROM CourseGrade course WHERE course.course.course.code = ?1")
    CourseGrade findByCourseCode(String courseCode);
}
