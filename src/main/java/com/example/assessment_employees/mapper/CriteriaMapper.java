package com.example.assessment_employees.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.assessment_employees.dto.request.CriteriaRequest;
import com.example.assessment_employees.dto.response.CriteriaResponse;
import com.example.assessment_employees.entity.CriteriaBank;

@Mapper(componentModel = "spring")
public interface CriteriaMapper {
    CriteriaResponse toResponse(CriteriaBank criteria);

    @Mapping(target = "criteriaId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CriteriaBank toEntity(CriteriaRequest criteriaRequest);
} 