package com.example.assessment_employees.service;

import com.example.assessment_employees.dto.response.AssessmentResultDetailResponse;
import com.example.assessment_employees.dto.response.ReportResponse;
import com.example.assessment_employees.entity.AssessmentResult;
import com.example.assessment_employees.entity.Department;
import com.example.assessment_employees.entity.User;
import com.example.assessment_employees.exception.ResourceNotFoundException;
import com.example.assessment_employees.repository.AssessmentResultRepository;
import com.example.assessment_employees.repository.DepartmentRepository;
import com.example.assessment_employees.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReportService {
    UserRepository userRepository;
    DepartmentRepository departmentRepository;
    AssessmentResultService assessmentResultService;
    UserService userService;
    AssessmentResultRepository assessmentResultRepository;

    public ReportResponse getAllResultInDepartment(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        List<User> users = userRepository.findByDepartment_DepartmentId(departmentId);

        Map<Integer, List<AssessmentResultDetailResponse>> maps =
                users.stream()
                        .collect(Collectors.toMap(
                        User::getUserId,
                        user -> assessmentResultService.getEmployeeAssessmentResults(user.getUserId())
                ));

        return ReportResponse.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .description(department.getDescription())
                .assessmentResultDetails(maps)
                .build();
    }
}
