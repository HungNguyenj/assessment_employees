package com.example.assessment_employees.controller;


import com.example.assessment_employees.dto.ApiResponse;
import com.example.assessment_employees.dto.response.AssessmentResultDetailRespone;
import com.example.assessment_employees.entity.AssessmentResultDetail;
import com.example.assessment_employees.service.AssessmentResultDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment-results-details")
@RequiredArgsConstructor
public class AssessmentResultDetailController {

    private final AssessmentResultDetailService service;
    private final AssessmentResultDetailService assessmentResultDetailService;

    @Transactional
    // API lấy tất cả chi tiết đánh giá
    @GetMapping
    public ResponseEntity<List<AssessmentResultDetail>> getAllDetails() {
        List<AssessmentResultDetail> details = service.getAllAssessmentResultDetail();
        return ResponseEntity.ok(details);
    }

    @Transactional
    // API lấy chi tiết đánh giá theo id
    @GetMapping("/{id}")
    public ResponseEntity<AssessmentResultDetail> getDetailById(@PathVariable Integer id) {
        AssessmentResultDetail detail = service.getAssessmentResultDetailById(id);
        return ResponseEntity.ok(detail);
    }

    // API tạo chi tiết đánh giá
    @Transactional
    @PostMapping
    public ResponseEntity<AssessmentResultDetail> createDetail(@RequestBody AssessmentResultDetailRespone dto) {
        AssessmentResultDetail newDetail = service.createAssessmentResultDetail(dto);
        return ResponseEntity.ok(newDetail);
    }

    // API cập nhật chi tiết đánh giá
    @PutMapping("/{detailId}")
    public ResponseEntity<ApiResponse<AssessmentResultDetail>> updateAssessmentResultDetail(
            @PathVariable Integer detailId,
            @Valid @RequestBody AssessmentResultDetailRespone assessmentResultDetailDto) {

        try {
            AssessmentResultDetail updatedDetail = assessmentResultDetailService.updateAssessmentResultDetail(detailId, assessmentResultDetailDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Assessment result detail updated successfully", updatedDetail));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Error: " + e.getMessage(), null));
        }
    }

    // API xóa chi tiết đánh giá
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDetail(@PathVariable Integer id) {
        service.deleteAssessmentResultDetail(id);
        return ResponseEntity.ok("Detail deleted successfully");
    }
}
