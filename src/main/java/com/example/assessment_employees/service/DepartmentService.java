package com.example.assessment_employees.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.assessment_employees.dto.response.DepartmentResponse;
import com.example.assessment_employees.entity.Department;
import com.example.assessment_employees.entity.User;
import com.example.assessment_employees.entity.UserRole;
import com.example.assessment_employees.mapper.DepartmentMapper;
import com.example.assessment_employees.repository.DepartmentRepository;
import com.example.assessment_employees.security.AuthorizationService;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;
    
    @Autowired
    private AuthorizationService authorizationService;

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
            .stream()
            .map(departmentMapper::toResponse)
            .collect(Collectors.toList());
            
    }
    
    public List<DepartmentResponse> getAccessibleDepartments() {
        User currentUser = authorizationService.getCurrentUser();
        
        // SUP có thể xem tất cả departments
        if (currentUser.getRole() == UserRole.SUPERVISOR) {
            return getAllDepartments();
        }
        
        // MANA và EMPL chỉ xem department của mình
        if (currentUser.getDepartment() != null) {
            Department userDepartment = currentUser.getDepartment();
            return Arrays.asList(departmentMapper.toResponse(userDepartment));
        }
        
        return Arrays.asList();
    }
}