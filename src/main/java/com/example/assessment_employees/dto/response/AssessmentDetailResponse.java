package com.example.assessment_employees.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDetailResponse {
    private Integer detailId;
    private Integer criteriaId;
    private String criteriaName;
    private Integer score;
    private String comments;
}