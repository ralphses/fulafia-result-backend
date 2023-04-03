package com.clicks.fulafiaresultcheckingverificationsystem.repository;

import com.clicks.fulafiaresultcheckingverificationsystem.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT student FROM Student student WHERE student.matric = ?1")
    Optional<Student> findStudentByMatric(String matric);

    @Query(value = "SELECT student FROM Student student WHERE student.phone = ?1")
    Optional<Student> findStudentByPhone(String phone);

}
