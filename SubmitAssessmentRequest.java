package com.example.assessment_employees.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.example.assessment_employees.entity.AssessmentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubmitAssessmentRequest {
    private Integer resultId;
    private Integer assessedUserId;
    private Integer assessorUserId;
    private Integer templateId;
    private String assessmentPeriod;
    private String comment;
    private AssessmentStatus status;
    private List<AssessmentDetailRequest> details;
}