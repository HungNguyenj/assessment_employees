package com.example.assessment_employees.mapper;

import com.example.assessment_employees.dto.response.AssessmentResultDetailResponse;
import com.example.assessment_employees.dto.response.CriteriaResultResponse;
import com.example.assessment_employees.entity.AssessmentResult;
import com.example.assessment_employees.entity.AssessmentResultDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssessmentResultDetailMapper {

    public static AssessmentResultDetailResponse toDetailResponse(AssessmentResult result, List<AssessmentResultDetail> details) {
        return AssessmentResultDetailResponse.builder()
                .id(result.getResultId())
                .assessmentId(result.getTemplate().getTemplateId())
                .employeeId(result.getAssessedUser().getUserId())
                .employeeName(result.getAssessedUser().getFullName())
                .submissionDate(result.getCreatedAt())
                .totalScore(result.getTotalScore().doubleValue())
                .overallComment(result.getComment())
                .sentimentLabel(result.getSentimentLabel())
                .sentimentScore(result.getSentimentScore() != null ? result.getSentimentScore().doubleValue() : null)
                .criteriaResults(details.stream().map(d -> CriteriaResultResponse.builder()
                        .criteriaId(d.getCriteria().getCriteriaId())
                        .criteriaName(d.getCriteria().getCriteriaName())
                        .criteriaDescription(d.getCriteria().getDescription())
                        .score(d.getScore().doubleValue())
                        .comment(d.getComments())
                        .maxScore(d.getCriteria().getDefaultMaxScore().doubleValue())
                        .build()
                ).collect(Collectors.toList()))
                .build();
    }
}
