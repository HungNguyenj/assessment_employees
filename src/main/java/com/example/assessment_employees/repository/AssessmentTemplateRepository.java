package com.example.assessment_employees.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.assessment_employees.entity.AssessmentTemplate;

@Repository
public interface AssessmentTemplateRepository extends JpaRepository<AssessmentTemplate, Integer> {
    @Query("SELECT DISTINCT t FROM AssessmentTemplate t " +
           "LEFT JOIN FETCH t.templateCriteriaMappings m " +
           "LEFT JOIN FETCH m.criteria " +
           "WHERE t.templateId = :id")
    Optional<AssessmentTemplate> findByIdWithCriteria(@Param("id") Integer id);

    @Query("SELECT DISTINCT t FROM AssessmentTemplate t " +
           "LEFT JOIN FETCH t.templateCriteriaMappings m " +
           "LEFT JOIN FETCH m.criteria")
    List<AssessmentTemplate> findAllWithCriteria();


}
