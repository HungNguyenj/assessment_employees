package com.example.assessment_employees.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.assessment_employees.dto.request.AssessmentDetailRequest;
import com.example.assessment_employees.dto.request.SubmitAssessmentRequest;
import com.example.assessment_employees.dto.response.AssessmentDetailResponse;
import com.example.assessment_employees.dto.response.AssessmentResultDetailResponse;
import com.example.assessment_employees.dto.response.AssessmentResultResponse;
import com.example.assessment_employees.dto.response.CriteriaResultResponse;
import com.example.assessment_employees.entity.AssessmentResult;
import com.example.assessment_employees.entity.AssessmentResultDetail;
import com.example.assessment_employees.entity.AssessmentStatus;
import com.example.assessment_employees.entity.AssessmentTemplate;
import com.example.assessment_employees.entity.CriteriaBank;
import com.example.assessment_employees.entity.TemplateCriteriaMapping;
import com.example.assessment_employees.entity.User;
import com.example.assessment_employees.exception.ResourceNotFoundException;
import com.example.assessment_employees.repository.AssessmentResultDetailRepository;
import com.example.assessment_employees.repository.AssessmentResultRepository;
import com.example.assessment_employees.repository.AssessmentTemplateRepository;
import com.example.assessment_employees.repository.CriteriaBankRepository;
import com.example.assessment_employees.repository.TemplateCriteriaMappingRepository;
import com.example.assessment_employees.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssessmentResultService {
    private final AssessmentResultRepository assessmentResultRepository;
    private final AssessmentResultDetailRepository resultDetailRepository;
    private final UserRepository userRepository;
    private final AssessmentTemplateRepository templateRepository;
    private final CriteriaBankRepository criteriaBankRepository;
    private final TemplateCriteriaMappingRepository templateCriteriaMappingRepository;


    @Transactional
    public AssessmentResultResponse submitAssessmentResult(SubmitAssessmentRequest request) {
        // Validate user and template
        User assessedUser = userRepository.findById(request.getAssessedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessed user not found with ID: " + request.getAssessedUserId()));
        
        User assessorUser = userRepository.findById(request.getAssessorUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessor user not found with ID: " + request.getAssessorUserId()));
        
        AssessmentTemplate template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with ID: " + request.getTemplateId()));
        
        // Create assessment result
        AssessmentResult result = new AssessmentResult();
        result.setAssessedUser(assessedUser);
        result.setAssessorUser(assessorUser);
        result.setTemplate(template);
        result.setAssessmentPeriod(request.getAssessmentPeriod());
        result.setComment(request.getComment());
        result.setStatus(AssessmentStatus.SUBMITTED); // Set status to SUBMITTED
        
        // Calculate total score
        BigDecimal totalScore = calculateTotalScore(request.getDetails(), template.getTemplateId());
        result.setTotalScore(totalScore);
        
        // Save the result
        result = assessmentResultRepository.save(result);
        
        // Create and save assessment details
        List<AssessmentResultDetail> details = new ArrayList<>();
        List<AssessmentDetailResponse> detailResponses = new ArrayList<>();
        
        for (AssessmentDetailRequest detailRequest : request.getDetails()) {
            CriteriaBank criteria = criteriaBankRepository.findById(detailRequest.getCriteriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Criteria not found with ID: " + detailRequest.getCriteriaId()));
            
            AssessmentResultDetail detail = new AssessmentResultDetail();
            detail.setResult(result);
            detail.setCriteria(criteria);
            detail.setScore(detailRequest.getScore());
            detail.setComments(detailRequest.getComments());
            
            detail = resultDetailRepository.save(detail);
            details.add(detail);
            
            detailResponses.add(AssessmentDetailResponse.builder()
                    .detailId(detail.getDetailId())
                    .criteriaId(criteria.getCriteriaId())
                    .criteriaName(criteria.getCriteriaName())
                    .score(detail.getScore())
                    .comments(detail.getComments())
                    .build());
        }
        
        // Build response
        return AssessmentResultResponse.builder()
                .resultId(result.getResultId())
                .assessedUserId(assessedUser.getUserId())
                .assessedUserName(assessedUser.getFullName())
                .assessorUserId(assessorUser.getUserId())
                .assessorUserName(assessorUser.getFullName())
                .templateId(template.getTemplateId())
                .templateName(template.getTemplateName())
                .assessmentPeriod(result.getAssessmentPeriod())
                .totalScore(result.getTotalScore())
                .status(result.getStatus())
                .comment(result.getComment())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .details(detailResponses)
                .build();
    }
    
    private BigDecimal calculateTotalScore(List<AssessmentDetailRequest> details, Integer templateId) {
        if (details == null || details.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal totalPoints = BigDecimal.ZERO;
        BigDecimal maxPossiblePoints = BigDecimal.ZERO;
        
        // Get all template-criteria mappings for this template
        List<TemplateCriteriaMapping> mappings = templateCriteriaMappingRepository.findByTemplate_TemplateId(templateId);
        
        // Create a map for quick lookups
        java.util.Map<Integer, Integer> criteriaMaxScores = mappings.stream()
                .collect(java.util.stream.Collectors.toMap(
                        mapping -> mapping.getCriteria().getCriteriaId(),
                        TemplateCriteriaMapping::getMaxScore
                ));
        
        for (AssessmentDetailRequest detail : details) {
            Integer criteriaId = detail.getCriteriaId();
            Integer maxScore = criteriaMaxScores.get(criteriaId);
            
            if (maxScore != null) {
                totalPoints = totalPoints.add(BigDecimal.valueOf(detail.getScore()));
                maxPossiblePoints = maxPossiblePoints.add(BigDecimal.valueOf(maxScore));
            }
        }
        
        // Calculate percentage and scale to 10
        if (maxPossiblePoints.compareTo(BigDecimal.ZERO) > 0) {
            return totalPoints.multiply(BigDecimal.TEN)
                    .divide(maxPossiblePoints, 2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public AssessmentResultDetailResponse getAssessmentResultDetail(Integer assessmentId) {
        // Implementation needed
        AssessmentResult result = assessmentResultRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment result not found with ID: " + assessmentId));
        
        List<AssessmentResultDetail> details = resultDetailRepository.findByResult_ResultId(assessmentId);
        
        List<CriteriaResultResponse> criteriaResults = new ArrayList<>();
        for (AssessmentResultDetail detail : details) {
            CriteriaResultResponse criteriaResult = CriteriaResultResponse.builder()
                    .criteriaId(detail.getCriteria().getCriteriaId())
                    .criteriaName(detail.getCriteria().getCriteriaName())
                    .criteriaDescription(detail.getCriteria().getDescription())
                    .score(detail.getScore().doubleValue())
                    .comment(detail.getComments())
                    .maxScore(detail.getCriteria().getDefaultMaxScore().doubleValue())
                    .build();
            criteriaResults.add(criteriaResult);
        }
        
        return AssessmentResultDetailResponse.builder()
                .id(result.getResultId())
                .assessmentId(result.getTemplate().getTemplateId())
                .employeeId(result.getAssessedUser().getUserId())
                .employeeName(result.getAssessedUser().getFullName())
                .submissionDate(result.getCreatedAt())
                .totalScore(result.getTotalScore().doubleValue())
                .overallComment(result.getComment())
                .criteriaResults(criteriaResults)
                .build();
    }
    
    public List<AssessmentResultDetailResponse> getEmployeeAssessmentResults(Integer employeeId) {
        // Implementation needed
        List<AssessmentResult> results = assessmentResultRepository.findByAssessedUser_UserId(employeeId);
        List<AssessmentResultDetailResponse> resultResponses = new ArrayList<>();
        for (AssessmentResult result : results) {
            resultResponses.add(getAssessmentResultDetail(result.getResultId()));
        }
        return resultResponses;
    }

    public String deleteAssessmentResult(Integer assessmentResultId) {
        assessmentResultRepository.deleteById(assessmentResultId);
        return "successfully";
    }
}
