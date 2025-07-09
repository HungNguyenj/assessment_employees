package com.example.assessment_employees.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaResultResponse {
    private Integer criteriaId;
    private String criteriaName;
    private String criteriaDescription;
    private Double score;
    private String comment;
    private Double maxScore;
    private Double weight;
}