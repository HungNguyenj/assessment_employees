package com.example.assessment_employees.dto.response;

import lombok.Data;

@Data
public class TemplateCriteriaMappingResponse {
    private Integer mappingId;
    private CriteriaResponse criteria;
    private Integer maxScore;
    private Integer criteriaOrder;
} 