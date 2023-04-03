package com.clicks.fulafiaresultcheckingverificationsystem.repository;

import com.clicks.fulafiaresultcheckingverificationsystem.model.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(value = "SELECT department FROM Department department WHERE department.name = ?1")
    Optional<Department> findDepartmentByName(String name);

    @Query(value = "SELECT department FROM Department department WHERE department.code = ?1")
    Optional<Department> findDepartmentByCode(String code);
}
