package com.example.assessment_employees.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.assessment_employees.dto.request.AssessmentResultRequest;
import com.example.assessment_employees.dto.request.UpdateAssessmentRequest;
import com.example.assessment_employees.mapper.AssessmentResultMapper;
import com.example.assessment_employees.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private SentimentService sentimentService;
    @Autowired
    private AssessmentResultMapper assessmentResultMapper;

    public AssessmentResult saveAssessment(AssessmentResult result) {
        if (result.getComment() != null && !result.getComment().isEmpty()) {
            SentimentResponse sentiment = sentimentService.analyzeComment(result.getComment());

            if (sentiment != null) {
                result.setSentimentLabel(sentiment.getLabel());
                result.setSentimentScore(sentiment.getScore());
            }
        }

        return assessmentResultRepository.save(result);
    }


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
        result = saveAssessment(result);

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
                .sentimentLabel(result.getSentimentLabel())   // 👈
                .sentimentScore(result.getSentimentScore())   // 👈
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
                .sentimentLabel(result.getSentimentLabel())
                .sentimentScore(result.getSentimentScore() != null ? result.getSentimentScore().doubleValue() : null)
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

    @Transactional
    public String deleteAssessmentResult(Integer resultId) {
        assessmentResultRepository.deleteById(resultId);
        return "Assessment result deleted successfully";
    }

    @Transactional
    public int updateAllCommentsWithSentiment() {
        List<AssessmentResult> allResults = assessmentResultRepository.findAll();
        int updatedCount = 0;

        for (AssessmentResult result : allResults) {
            String comment = result.getComment();
            if (comment != null && !comment.trim().isEmpty()) {
                SentimentResponse sentiment = sentimentService.analyzeComment(comment);
                if (sentiment != null) {
                    result.setSentimentLabel(sentiment.getLabel());
                    result.setSentimentScore(sentiment.getScore());
                    updatedCount++;
                }
            }
        }

        // Cập nhật tất cả cùng lúc
        assessmentResultRepository.saveAll(allResults);
        return updatedCount;
    }

    @Transactional
    public AssessmentResultDetailResponse updateAssessmentResult(UpdateAssessmentRequest request) {
        AssessmentResult existing = assessmentResultRepository.findById(request.getResultId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài đánh giá: " + request.getResultId()));

        // Cập nhật nhận xét
        existing.setComment(request.getComment());
        existing.setStatus(AssessmentStatus.SUBMITTED);
        existing.setUpdatedAt(LocalDateTime.now());

        // Xoá detail cũ
        resultDetailRepository.deleteById(existing.getResultId());

        // Tạo lại chi tiết
        List<AssessmentResultDetail> details = request.getDetails().stream().map(d -> {
            AssessmentResultDetail detail = new AssessmentResultDetail();
            detail.setResult(existing);
            detail.setCriteria(criteriaBankRepository.findById(d.getCriteriaId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tiêu chí với ID: " + d.getCriteriaId())));
            detail.setScore((d.getScore().intValue()));
            return detail;
        }).toList();

        resultDetailRepository.saveAll(details);

        // Tính điểm trung bình

        existing.setTotalScore(request.getTotalScore());

        // Gọi phân tích cảm xúc
        SentimentResponse sentiment = sentimentService.analyzeComment(existing.getComment());
        existing.setSentimentLabel(sentiment.getLabel());
        existing.setSentimentScore(sentiment.getScore());

        // Lưu lại
        AssessmentResult saved = assessmentResultRepository.save(existing);

        // Trả lại chi tiết để hiển thị đúng trên giao diện
        return getAssessmentResultDetail(saved.getResultId());
    }


}
