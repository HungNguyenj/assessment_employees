package com.example.assessment_employees.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CriteriaRequest {
    @NotNull(message = "Criteria name cannot be null")
    private String criteriaName;
    
    private String category;
    
    private String description;
    
    @Min(value = 1, message = "Default max score must be at least 1")
    private Integer defaultMaxScore = 10;
    
    private Boolean isActive = true;
}