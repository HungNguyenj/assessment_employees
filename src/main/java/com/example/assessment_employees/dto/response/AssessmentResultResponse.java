package com.example.assessment_employees.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.assessment_employees.entity.AssessmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultResponse {
    private Integer resultId;
    private Integer assessedUserId;
    private String assessedUserName;
    private Integer assessorUserId;
    private String assessorUserName;
    private Integer templateId;
    private String templateName;
    private String assessmentPeriod;
    private BigDecimal totalScore;
    private AssessmentStatus status;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AssessmentDetailResponse> details;
}