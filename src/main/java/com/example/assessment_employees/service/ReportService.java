package com.example.assessment_employees.service;

import com.example.assessment_employees.dto.response.DepartmentReportResponse;
import com.example.assessment_employees.dto.response.UserReportResponse;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReportService {
    UserRepository userRepository;
    DepartmentRepository departmentRepository;
    AssessmentResultService assessmentResultService;
    UserService userService;
    AssessmentResultRepository assessmentResultRepository;


    public UserReportResponse generateIndividualReport
            (Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<AssessmentResult> results = assessmentResultRepository.findByAssessedUser_UserId(userId);

        BigDecimal averageScore = calculateAverageScore(results);
        String rating = ratingEmployee(averageScore);

        return UserReportResponse.builder()
                .employeeName(user.getFullName())
                .averageScore(averageScore.doubleValue())
                .rating(rating)
                .totalAssessments(results.size())
                .build();
    }

    public List<UserReportResponse> generateGroupReport(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        List<User> users = userRepository.findByDepartment_DepartmentId(department.getDepartmentId());
        List<UserReportResponse> groupReports = new ArrayList<>();

        for (User user : users) {
            List<AssessmentResult> results = assessmentResultRepository
                    .findByAssessedUser_UserId(user.getUserId());
            BigDecimal averageScore = calculateAverageScore(results);
            String rating = ratingEmployee(averageScore);
            groupReports.add(UserReportResponse.builder()
                            .employeeName(user.getFullName())
                            .averageScore(averageScore.doubleValue())
                            .totalAssessments(results.size())
                            .rating(rating)
                    .build());
        }
        return groupReports;
    }

    public DepartmentReportResponse generateDepartmentReport(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        List<UserReportResponse> usersReports = generateGroupReport(department.getDepartmentId());

        double avgScore = usersReports.stream()
                .mapToDouble(UserReportResponse::getAverageScore)
                .average()
                .orElse(0.0);

        return DepartmentReportResponse.builder()
                .departmentName(department.getDepartmentName())
                .avgDepartmentScore(avgScore)
                .rating(ratingDepartment(avgScore))
                .totalUsers(usersReports.size())
                .usersReports(usersReports)
                .build();
    }

    private BigDecimal calculateAverageScore(List<AssessmentResult> results) {
        BigDecimal total = BigDecimal.ZERO;
        for (AssessmentResult result : results) {
            total = total.add(result.getTotalScore());
        }
        return total.divide(BigDecimal.valueOf(results.size()));
    }

    private String ratingEmployee(BigDecimal averageScore) {
        double score = averageScore.doubleValue();
        if (score >= 8.0 && score <= 10.0) {
            return "Excellent";
        } else if (score >= 7.0) {
            return "Good";
        } else if (score >= 5.0) {
            return "Average";
        } else {
            return "Poor";
        }
    }

    private String ratingDepartment(double score) {
        if (score >= 8.0 && score <= 10.0) {
            return "Excellent";
        } else if (score >= 7.0) {
            return "Good";
        } else if (score >= 5.0) {
            return "Average";
        } else {
            return "Poor";
        }
    }
}
