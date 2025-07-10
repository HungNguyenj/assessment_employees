package com.example.assessment_employees.controller;

import java.util.List;
import java.util.Map;

import com.example.assessment_employees.dto.request.AssessmentResultRequest;
import com.example.assessment_employees.dto.request.UpdateAssessmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.assessment_employees.dto.ApiResponse;
import com.example.assessment_employees.dto.request.SubmitAssessmentRequest;
import com.example.assessment_employees.dto.response.AssessmentResultDetailResponse;
import com.example.assessment_employees.dto.response.AssessmentResultResponse;
import com.example.assessment_employees.service.AssessmentResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssessmentResultController {

    @Autowired
    private AssessmentResultService assessmentResultService;


    // API nộp kết quả đánh giá
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<AssessmentResultResponse>> submitAssessmentResult(
            @RequestBody SubmitAssessmentRequest request) {
        AssessmentResultResponse result = assessmentResultService.submitAssessmentResult(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Assessment result submitted successfully", result));
    }

    // API lấy kết quả đánh giá chi tiết (tiêu chí, điểm và bình luận)
    @GetMapping("/{assessmentId}/results/detail")
    public ResponseEntity<ApiResponse<AssessmentResultDetailResponse>> getAssessmentResultDetail(
            @PathVariable Integer assessmentId) {
        AssessmentResultDetailResponse resultDetail = assessmentResultService.getAssessmentResultDetail(assessmentId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Assessment result details retrieved successfully", resultDetail));
    }

    // API lấy kết quả đánh giá chi tiết của một nhân viên
    @GetMapping("/results/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<AssessmentResultDetailResponse>>> getEmployeeAssessmentResults(
            @PathVariable Integer employeeId) {
        List<AssessmentResultDetailResponse> results = assessmentResultService.getEmployeeAssessmentResults(employeeId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee assessment results retrieved successfully", results));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAssessmentResult(@PathVariable Integer id) {
        String message = assessmentResultService.deleteAssessmentResult(id);
        return ResponseEntity.ok(message);
    }

    @PutMapping("/update-comments-sentiment")
    public ResponseEntity<String> updateAllSentimentComments() {
        int updated = assessmentResultService.updateAllCommentsWithSentiment();
        return ResponseEntity.ok("Đã cập nhật " + updated + " đánh giá với phân tích cảm xúc.");
    }

    // API cập nhật kết quả đánh giá (khi chỉnh sửa)
    @PutMapping("/save")
    public ResponseEntity<ApiResponse<AssessmentResultDetailResponse>> saveAssessment(@RequestBody UpdateAssessmentRequest request) {
        AssessmentResultDetailResponse response = assessmentResultService.updateAssessmentResult(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Assessment updated", response));
    }


}