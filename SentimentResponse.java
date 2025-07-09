package com.example.assessment_employees.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class SentimentResponse {
    private String label;
    private BigDecimal score;

    // getters và setters
}

