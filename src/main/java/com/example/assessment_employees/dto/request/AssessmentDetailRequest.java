package com.example.assessment_employees.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDetailRequest {
    private Integer criteriaId;
    private Integer score;
    private String comments;
}