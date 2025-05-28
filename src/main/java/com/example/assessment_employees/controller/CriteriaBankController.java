package com.example.assessment_employees.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.assessment_employees.dto.ApiResponse;
import com.example.assessment_employees.dto.request.CriteriaRequest;
import com.example.assessment_employees.dto.response.CriteriaResponse;
import com.example.assessment_employees.service.CriteriaBankService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/criteria")
@RequiredArgsConstructor
public class CriteriaBankController {
    private final CriteriaBankService criteriaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CriteriaResponse>>> getAllCriteria() {
        List<CriteriaResponse> criteria = criteriaService.getAllCriteria();
        return ResponseEntity.ok(new ApiResponse<>(true, "Criteria retrieved successfully", criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CriteriaResponse>> getCriteriaById(@PathVariable Integer id) {
        CriteriaResponse criteria = criteriaService.getCriteriaById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Criteria retrieved successfully", criteria));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CriteriaResponse>> createCriteria(@Valid @RequestBody CriteriaRequest criteriaRequest) {
        CriteriaResponse createdCriteria = criteriaService.createCriteria(criteriaRequest);
        return new ResponseEntity<>(
            new ApiResponse<>(true, "Criteria created successfully", createdCriteria),
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CriteriaResponse>> updateCriteria(
            @PathVariable Integer id,
            @Valid @RequestBody CriteriaRequest criteriaRequest) {
        CriteriaResponse updatedCriteria = criteriaService.updateCriteria(id, criteriaRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Criteria updated successfully", updatedCriteria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCriteria(@PathVariable Integer id) {
        criteriaService.deleteCriteria(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Criteria deleted successfully", null));
    }
} 