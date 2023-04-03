package com.clicks.fulafiaresultcheckingverificationsystem.repository;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.Course;
import com.clicks.fulafiaresultcheckingverificationsystem.model.student.StudentResultCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentResultCourseRepository extends JpaRepository<StudentResultCourse, Long> {

    @Query(value = "SELECT result FROM StudentResultCourse result WHERE result.currentSemester = ?1 AND result.currentSession = ?2 AND result.course = ?3")
    Optional<StudentResultCourse> findByCurrentSemesterAndCurrentSessionAndCourse(Semester semester, String session, Course course);
}
