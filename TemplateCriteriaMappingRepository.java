package com.example.assessment_employees.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.assessment_employees.entity.TemplateCriteriaMapping;

@Repository
public interface TemplateCriteriaMappingRepository extends JpaRepository<TemplateCriteriaMapping, Integer> {
    void deleteByTemplate_TemplateId(Integer templateId);
    
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM TemplateCriteriaMapping m " +
           "WHERE m.template.templateId = :templateId AND m.criteria.criteriaId = :criteriaId")
    boolean existsByTemplate_TemplateIdAndCriteria_CriteriaId(@Param("templateId") Integer templateId, 
                                                             @Param("criteriaId") Integer criteriaId);
    
    @Query("SELECT MAX(m.criteriaOrder) FROM TemplateCriteriaMapping m WHERE m.template.templateId = :templateId")
    Integer findMaxOrderByTemplateId(@Param("templateId") Integer templateId);

    List<TemplateCriteriaMapping> findByTemplate_TemplateId(Integer templateId);


    @Modifying
    @Query("DELETE FROM TemplateCriteriaMapping m " +
            "WHERE m.template.templateId = :templateId AND m.criteria.criteriaId = :criteriaId")
    void deleteByTemplate_TemplateIdAndCriteria_CriteriaId(@Param("templateId") Integer templateId,
                                     @Param("criteriaId") Integer criteriaId);
}