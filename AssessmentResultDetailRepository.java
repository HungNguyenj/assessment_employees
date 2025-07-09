package com.example.assessment_employees.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.assessment_employees.entity.AssessmentResultDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssessmentResultDetailRepository extends JpaRepository<AssessmentResultDetail, Integer> {
    List<AssessmentResultDetail> findByResult_ResultId(Integer resultId);
    AssessmentResultDetail save(AssessmentResultDetail detail);

}
