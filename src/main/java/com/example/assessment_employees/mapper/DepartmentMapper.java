package com.example.assessment_employees.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.example.assessment_employees.dto.response.DepartmentResponse;
import com.example.assessment_employees.entity.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    
   DepartmentResponse toResponse(Department department);
} 