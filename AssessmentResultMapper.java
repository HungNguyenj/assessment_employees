package com.example.assessment_employees.mapper;

import com.example.assessment_employees.dto.response.AssessmentDetailResponse;
import com.example.assessment_employees.dto.response.AssessmentResultResponse;
import com.example.assessment_employees.entity.AssessmentResult;
import com.example.assessment_employees.entity.AssessmentResultDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssessmentResultMapper {

    public static AssessmentResultResponse toResponse(AssessmentResult result) {
        return AssessmentResultResponse.builder()
                .resultId(result.getResultId())
                .assessedUserId(result.getAssessedUser().getUserId())
                .assessedUserName(result.getAssessedUser().getFullName())
                .assessorUserId(result.getAssessorUser().getUserId())
                .assessorUserName(result.getAssessorUser().getFullName())
                .templateId(result.getTemplate().getTemplateId())
                .templateName(result.getTemplate().getTemplateName())
                .assessmentPeriod(result.getAssessmentPeriod())
                .comment(result.getComment())
                .totalScore(result.getTotalScore())
                .status(result.getStatus())
                .sentimentLabel(result.getSentimentLabel())
                .sentimentScore(result.getSentimentScore())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .details(mapDetails(result.getDetails()))
                .build();
    }

    private static List<AssessmentDetailResponse> mapDetails(List<AssessmentResultDetail> details) {
        if (details == null) return List.of();
        return details.stream()
                .map(detail -> AssessmentDetailResponse.builder()
                        .detailId(detail.getDetailId())
                        .criteriaId(detail.getCriteria().getCriteriaId())
                        .criteriaName(detail.getCriteria().getCriteriaName())
                        .score(detail.getScore())
                        .comments(detail.getComments())
                        .build()
                ).collect(Collectors.toList());
    }

}
