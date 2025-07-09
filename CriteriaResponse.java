package com.example.assessment_employees.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CriteriaResponse {
    private Integer criteriaId;
    private String criteriaName;
    private String category;
    private String description;
    private Integer defaultMaxScore;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 