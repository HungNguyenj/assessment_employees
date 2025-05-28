package com.example.assessment_employees.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.assessment_employees.dto.response.DepartmentResponse;
import com.example.assessment_employees.mapper.DepartmentMapper;
import com.example.assessment_employees.repository.DepartmentRepository;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
            .stream()
            .map(departmentMapper::toResponse)
            .collect(Collectors.toList());
            
    }
} 