package com.example.assessment_employees.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "template_criteria_mapping",
       uniqueConstraints = @UniqueConstraint(columnNames = {"template_id", "criteria_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateCriteriaMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_id")
    private Integer mappingId;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private AssessmentTemplate template;

    @ManyToOne
    @JoinColumn(name = "criteria_id", nullable = false)
    private CriteriaBank criteria;

    @NotNull
    @Column(name = "max_score", nullable = false)
    private Integer maxScore = 5;

    @Column(name = "criteria_order")
    private Integer criteriaOrder = 0;
} 