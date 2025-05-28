package com.example.assessment_employees.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.assessment_employees.dto.ApiResponse;
import com.example.assessment_employees.dto.request.AssessmentTemplateRequest;
import com.example.assessment_employees.dto.response.AssessmentTemplateResponse;
import com.example.assessment_employees.service.AssessmentTemplateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class AssessmentTemplateController {
    private final AssessmentTemplateService templateService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssessmentTemplateResponse>>> getAllTemplates() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Templates retrieved successfully", 
                templateService.getAllTemplates()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssessmentTemplateResponse>> getTemplateById(@PathVariable Integer id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Template retrieved successfully", 
                templateService.getTemplateById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AssessmentTemplateResponse>> createTemplate(
            @Valid @RequestBody AssessmentTemplateRequest request) {
        return new ResponseEntity<>(new ApiResponse<>(true, "Template created successfully", 
                templateService.createTemplate(request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssessmentTemplateResponse>> updateTemplate(
            @PathVariable Integer id, @Valid @RequestBody AssessmentTemplateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Template updated successfully", 
                templateService.updateTemplate(id, request)));
    }

    @PostMapping("/{id}/clone")
    public ResponseEntity<ApiResponse<AssessmentTemplateResponse>> cloneTemplate(@PathVariable Integer id) {
        return new ResponseEntity<>(new ApiResponse<>(true, "Template cloned successfully", 
                templateService.cloneTemplate(id)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Integer id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Template deleted successfully", null));
    }

    @PostMapping("/{id}/criteria/{criteriaId}")
    public ResponseEntity<ApiResponse<AssessmentTemplateResponse>> addCriteriaToTemplate(
            @PathVariable Integer id, @PathVariable Integer criteriaId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Criteria added to template successfully", 
                templateService.addCriteriaToTemplate(id, List.of(criteriaId))));
    }

    @DeleteMapping("/{templateId}/criteria/{criteriaId}")
    public ResponseEntity<ApiResponse<AssessmentTemplateResponse>> removeCriteriaFromTemplate(
            @PathVariable Integer templateId, @PathVariable Integer criteriaId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Criteria removed from template successfully", 
                templateService.removeCriteriaFromTemplate(templateId, criteriaId)));
    }

    @PostMapping("/{id}/criteria/batch")
    public ResponseEntity<ApiResponse<AssessmentTemplateResponse>> addCriteriaBatchToTemplate(
            @PathVariable Integer id,
            @RequestBody List<Integer> criteriaIds) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Criteria added to template successfully", 
                templateService.addCriteriaToTemplate(id, criteriaIds)));
    }


} 