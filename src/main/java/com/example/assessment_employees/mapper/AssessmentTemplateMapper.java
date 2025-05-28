package com.example.assessment_employees.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.assessment_employees.dto.request.AssessmentTemplateRequest;
import com.example.assessment_employees.dto.response.AssessmentTemplateResponse;
import com.example.assessment_employees.entity.AssessmentTemplate;

@Mapper(componentModel = "spring", uses = {TemplateCriteriaMappingMapper.class})
public interface AssessmentTemplateMapper {
    @Mapping(target = "templateId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "templateCriteriaMappings", ignore = true)
    AssessmentTemplate toEntity(AssessmentTemplateRequest request);

    @Mapping(target = "criteria", source = "templateCriteriaMappings")
    AssessmentTemplateResponse toResponse(AssessmentTemplate template);
} 