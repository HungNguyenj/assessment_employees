package com.example.assessment_employees.controller;

import com.example.assessment_employees.dto.ApiResponse;
import com.example.assessment_employees.dto.response.DepartmentReportResponse;
import com.example.assessment_employees.dto.response.UserReportResponse;
import com.example.assessment_employees.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    ReportService reportService;

    @GetMapping("/individual")
    public ResponseEntity<ApiResponse<UserReportResponse>> getIndividualReport(
            @RequestParam Integer userId) {
        UserReportResponse report = reportService.generateIndividualReport(userId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Success", report)
        );
    }

    @GetMapping("/department")
    public ResponseEntity<ApiResponse<DepartmentReportResponse>> getDepartmentReport(
            @RequestParam Integer departmentId) {
        DepartmentReportResponse report = reportService.generateDepartmentReport(departmentId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Success", report)
        );
    }

    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<List<UserReportResponse>>> getReportByTemplate(
            @RequestParam Integer templateId) {
        List<UserReportResponse> report = reportService.generateReportByTemplate(templateId);
        ApiResponse<List<UserReportResponse>> response = ApiResponse.<List<UserReportResponse>>builder()
                .success(true)
                .message("Success")
                .data(report)
                .build();
        return ResponseEntity.ok(response);
    }


    /**
     * ✅ API lọc theo 1 trong 2 điều kiện: departmentId hoặc templateId
     * Trả về danh sách UserReportResponse (frontend sẽ hiển thị theo nhân viên)
     */
    @GetMapping("/evaluations/report")
    public ResponseEntity<ApiResponse<List<UserReportResponse>>> getEvaluationReport(
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer templateId) {
        List<UserReportResponse> report = reportService.generateEvaluationReport(departmentId, templateId);
        ApiResponse<List<UserReportResponse>> response = ApiResponse.<List<UserReportResponse>>builder()
                .success(true)
                .message("Success")
                .data(report)
                .build();
        return ResponseEntity.ok(response);
    }
}
