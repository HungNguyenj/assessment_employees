package com.example.assessment_employees.dto.response;

import com.example.assessment_employees.entity.AssessmentResult;
import com.example.assessment_employees.entity.CriteriaBank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AssessmentResultDetailRespone {
    private Integer detailId;
    private AssessmentResult result;
    private CriteriaBank criteria;
    private Integer score;
    private String comments;
}
