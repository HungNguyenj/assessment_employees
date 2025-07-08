package com.example.assessment_employees.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {
    String employeeName;
    double averageScore;
    String rating;
    int totalAssessments;
//    LocalDate periodStart;
//    LocalDate periodEnd;
}
