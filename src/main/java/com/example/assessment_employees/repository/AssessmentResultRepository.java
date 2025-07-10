package com.example.assessment_employees.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.assessment_employees.dto.response.AssessmentResultResponse;
import com.example.assessment_employees.entity.AssessmentResult;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Integer> {
    List<AssessmentResult> findByAssessedUser_UserId(Integer assessedUserUserId);
    // Trong AssessmentResultRepository
    Optional<AssessmentResult> findById(Integer id);
    List<AssessmentResult> findByAssessedUser_UserIdAndTemplate_TemplateId(Integer userId, Integer templateId);

}

