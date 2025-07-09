package com.example.assessment_employees.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "assessment_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssessmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "assessed_user_id", nullable = false)
    private User assessedUser;

    @ManyToOne
    @JoinColumn(name = "assessor_user_id", nullable = false)
    private User assessorUser;

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private AssessmentTemplate template;

    @Column(name = "assessment_period")
    private String assessmentPeriod;

    @Column(name = "total_score", precision = 10, scale = 2)
    private BigDecimal totalScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AssessmentStatus status = AssessmentStatus.DRAFT;

    @Column(name = "comment")
    private String comment;

    @Column(name = "sentiment_label")
    private String sentimentLabel; // VD: POSITIVE, NEGATIVE, NEUTRAL

    @Column(name = "sentiment_score", precision = 5, scale = 4)
    private BigDecimal sentimentScore; // VD: 0.8231

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "result", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AssessmentResultDetail> details;
}