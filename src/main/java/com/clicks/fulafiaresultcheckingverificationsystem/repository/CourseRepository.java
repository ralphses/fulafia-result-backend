package com.clicks.fulafiaresultcheckingverificationsystem.repository;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.Level;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT course FROM Course course WHERE course.title = ?1")
    Optional<Course> findCourseByTitle(String title);

    @Query(value = "SELECT course FROM Course course WHERE course.code = ?1")
    Optional<Course> findCourseByCode(String code);

}
