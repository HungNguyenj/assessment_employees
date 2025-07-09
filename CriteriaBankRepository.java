package com.example.assessment_employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.assessment_employees.entity.CriteriaBank;

@Repository
public interface CriteriaBankRepository extends JpaRepository<CriteriaBank, Integer> {
    boolean existsCriteriaBankByCriteriaName(String criteriaName);
} 