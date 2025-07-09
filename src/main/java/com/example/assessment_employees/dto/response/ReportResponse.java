package com.example.assessment_employees.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Integer departmentId;
    private String departmentName;
    private String description;
    Map<Integer, List<AssessmentResultDetailResponse>> assessmentResultDetails;
}
