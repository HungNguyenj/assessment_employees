package com.example.assessment_employees.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.assessment_employees.dto.request.TemplateCriteriaMappingRequest;
import com.example.assessment_employees.dto.response.TemplateCriteriaMappingResponse;
import com.example.assessment_employees.entity.TemplateCriteriaMapping;

@Mapper(componentModel = "spring", uses = {CriteriaMapper.class})
public interface TemplateCriteriaMappingMapper {
    
    @Mapping(target = "mappingId", ignore = true)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "criteria.criteriaId", source = "criteriaId")
    TemplateCriteriaMapping toEntity(TemplateCriteriaMappingRequest request);

    @Mapping(target = "criteria", source = "criteria")
    TemplateCriteriaMappingResponse toResponse(TemplateCriteriaMapping entity);
}
