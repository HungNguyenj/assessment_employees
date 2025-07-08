package com.example.assessment_employees.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentReportResponse {
    String departmentName;
    double avgDepartmentScore;
    String rating;
    int totalUsers;
    List<UserReportResponse> usersReports;
}
