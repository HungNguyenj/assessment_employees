package com.example.assessment_employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.assessment_employees.entity.AssessmentResultDetail;

import java.util.List;

public interface AssessmentResultDetailRepository extends JpaRepository<AssessmentResultDetail, Integer> {
    List<AssessmentResultDetail> findByResult_ResultId(Integer resultId);
}
