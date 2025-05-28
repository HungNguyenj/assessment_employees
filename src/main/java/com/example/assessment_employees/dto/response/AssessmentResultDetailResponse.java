package com.example.assessment_employees.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultDetailResponse {
    private Integer id;
    private Integer assessmentId;
    private Integer employeeId;
    private String employeeName;
    private LocalDateTime submissionDate;
    private Double totalScore;
    private String overallComment;
    private List<CriteriaResultResponse> criteriaResults;
} 