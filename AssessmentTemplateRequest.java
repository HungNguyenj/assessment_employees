package com.example.assessment_employees.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssessmentTemplateRequest {
    @NotNull(message = "Template name cannot be null")
    private String templateName;
    
    private String description;
    
    private List<Integer> criteriaIds;
    
    private Integer defaultMaxScore = 10;
}