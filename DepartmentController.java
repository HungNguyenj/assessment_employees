package com.example.assessment_employees.controller;

import java.util.List;

import com.example.assessment_employees.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.assessment_employees.dto.response.DepartmentResponse;
import com.example.assessment_employees.service.DepartmentService;
import com.example.assessment_employees.security.AuthorizationService;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        List<DepartmentResponse> departments = departmentService.getAccessibleDepartments();
        return ResponseEntity.ok(new ApiResponse<>(true,"Get list of departments", departments));
    }
}
