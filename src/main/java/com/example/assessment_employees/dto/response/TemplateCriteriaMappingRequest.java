package com.example.assessment_employees.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TemplateCriteriaMappingRequest {
    @NotNull(message = "Criteria ID cannot be null")
    private Integer criteriaId;
    
    @NotNull(message = "Max score cannot be null")
    @Min(value = 1, message = "Max score must be at least 1")
    private Integer maxScore;
    
    private Integer criteriaOrder = 0;
} 