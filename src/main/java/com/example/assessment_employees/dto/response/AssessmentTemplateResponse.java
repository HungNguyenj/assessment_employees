package com.example.assessment_employees.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class AssessmentTemplateResponse {
    private Integer templateId;
    private String templateName;
    private String description;
    private Boolean isActive;
    private List<TemplateCriteriaMappingResponse> criteria;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 