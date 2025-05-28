package com.example.assessment_employees.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAssessmentRequest {
    private Integer assessedUserId;
    private Integer assessorUserId;
    private Integer templateId;
    private String assessmentPeriod;
    private String comment;
    private List<AssessmentDetailRequest> details;
}