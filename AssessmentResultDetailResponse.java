package com.example.assessment_employees.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssessmentResultDetailResponse {
    private Integer id;
    private Integer assessmentId;
    private Integer employeeId;
    private String employeeName;
    private LocalDateTime submissionDate;
    private Double totalScore;
    private String overallComment;

    private String sentimentLabel;
    private Double sentimentScore;

    private List<CriteriaResultResponse> criteriaResults;
} 