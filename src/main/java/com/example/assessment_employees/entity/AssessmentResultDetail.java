package com.example.assessment_employees.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assessment_result_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Integer detailId;

    @ManyToOne
    @JoinColumn(name = "result_id", nullable = false)
    private AssessmentResult result;

    @ManyToOne
    @JoinColumn(name = "criteria_id", nullable = false)
    private CriteriaBank criteria;

    @NotNull
    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;
} 