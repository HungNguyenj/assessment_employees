package com.example.assessment_employees.dto.request;

import com.example.assessment_employees.entity.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotNull(message = "Username cannot be null")
    private String username;
    
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;
    
    @NotNull(message = "Full name cannot be null")
    private String fullName;
    
    @NotNull(message = "Department ID cannot be null")
    private Integer departmentId;
    
    private UserRole role = UserRole.EMPLOYEE;
} 