package com.example.assessment_employees.dto.request;

import com.example.assessment_employees.entity.AssessmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AssessmentResultRequest {
    private Integer resultId;

    @NotNull
    private Integer assessedUserId;

    @NotNull
    private Integer assessorUserId;

    @NotNull
    private Integer templateId;

    private String assessmentPeriod;

    private BigDecimal totalScore;

    private AssessmentStatus status;
}
