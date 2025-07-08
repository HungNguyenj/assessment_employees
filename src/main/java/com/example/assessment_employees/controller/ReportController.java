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

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    ReportService reportService;

    @GetMapping("/individual")
    public ResponseEntity<ApiResponse<UserReportResponse>> getIndividualReport
            (@RequestParam Integer userId) {
        ApiResponse<UserReportResponse> apiResponse = ApiResponse.<UserReportResponse>builder()
                .success(true)
                .message("Success")
                .data(reportService.generateIndividualReport(userId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/department")
    public ResponseEntity<ApiResponse<DepartmentReportResponse>> getDepartmentReport
            (@RequestParam Integer departmentId) {
        ApiResponse<DepartmentReportResponse> apiResponse = ApiResponse.<DepartmentReportResponse>builder()
                .success(true)
                .message("Success")
                .data(reportService.generateDepartmentReport(departmentId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
