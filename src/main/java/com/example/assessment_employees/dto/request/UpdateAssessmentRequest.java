package com.example.assessment_employees.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateAssessmentRequest {
    private Integer resultId;  // ID của bài đánh giá cần update
    private String comment;    // Nhận xét mới (nếu có)
    private BigDecimal totalScore;
    private List<AssessmentDetailUpdate> details; // Danh sách điểm từng tiêu chí

    @Data
    public static class AssessmentDetailUpdate {
        private Integer criteriaId;  // <-- nên dùng criteriaId thay vì questionId
        private BigDecimal score;
    }
}
