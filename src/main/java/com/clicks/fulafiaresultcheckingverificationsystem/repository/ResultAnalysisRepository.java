package com.clicks.fulafiaresultcheckingverificationsystem.repository;

import com.clicks.fulafiaresultcheckingverificationsystem.model.student.ResultAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultAnalysisRepository extends JpaRepository<ResultAnalysis, Long> {
}
