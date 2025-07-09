package com.example.assessment_employees.dto.response;

import java.time.LocalDateTime;

import com.example.assessment_employees.entity.UserRole;

import lombok.Data;

@Data
public class UserResponse {
    private Integer userId;
    private String username;
    private String email;
    private String fullName;
    private DepartmentResponse department;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 