package com.example.assessment_employees.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.assessment_employees.dto.request.AssessmentTemplateRequest;
import com.example.assessment_employees.dto.response.AssessmentTemplateResponse;
import com.example.assessment_employees.entity.AssessmentTemplate;
import com.example.assessment_employees.entity.CriteriaBank;
import com.example.assessment_employees.entity.TemplateCriteriaMapping;
import com.example.assessment_employees.exception.ResourceNotFoundException;
import com.example.assessment_employees.mapper.AssessmentTemplateMapper;
import com.example.assessment_employees.repository.AssessmentTemplateRepository;
import com.example.assessment_employees.repository.CriteriaBankRepository;
import com.example.assessment_employees.repository.TemplateCriteriaMappingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssessmentTemplateService {
    private final AssessmentTemplateRepository templateRepository;
    private final CriteriaBankRepository criteriaRepository;
    private final TemplateCriteriaMappingRepository mappingRepository;
    private final AssessmentTemplateMapper templateMapper;

    public List<AssessmentTemplateResponse> getAllTemplates() {
        return templateRepository.findAllWithCriteria().stream()
                .map(templateMapper::toResponse)
                .collect(Collectors.toList());
    }

    public AssessmentTemplateResponse getTemplateById(Integer id) {
        AssessmentTemplate template = templateRepository.findByIdWithCriteria(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));
        return templateMapper.toResponse(template);
    }

    @Transactional
    public AssessmentTemplateResponse createTemplate(AssessmentTemplateRequest request) {
        AssessmentTemplate template = templateMapper.toEntity(request);
        template = templateRepository.save(template);

        if (request.getCriteriaIds() != null && !request.getCriteriaIds().isEmpty()) {
            createTemplateCriteriaMappings(template, request.getCriteriaIds(), request.getDefaultMaxScore());
        }

        return templateMapper.toResponse(templateRepository.findByIdWithCriteria(template.getTemplateId()).get());
    }

    @Transactional
    public AssessmentTemplateResponse updateTemplate(Integer id, AssessmentTemplateRequest request) {
        AssessmentTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + id));

        template.setTemplateName(request.getTemplateName());
        template.setDescription(request.getDescription());
        template = templateRepository.save(template);

        // Update criteria if provided
        if (request.getCriteriaIds() != null) {
            // Remove existing mappings
            mappingRepository.deleteByTemplate_TemplateId(id);
            
            // Create new mappings if criteria ids are provided
            if (!request.getCriteriaIds().isEmpty()) {
                createTemplateCriteriaMappings(template, request.getCriteriaIds(), request.getDefaultMaxScore());
            }
        }

        return templateMapper.toResponse(templateRepository.findById(id).get());
    }

    @Transactional
    public AssessmentTemplateResponse cloneTemplate(Integer sourceId) {
        AssessmentTemplate sourceTemplate = templateRepository.findById(sourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + sourceId));

        // Create new template
        AssessmentTemplate newTemplate = new AssessmentTemplate();
        newTemplate.setTemplateName(sourceTemplate.getTemplateName() + " (Copy)");
        newTemplate.setDescription(sourceTemplate.getDescription());
        newTemplate.setIsActive(true);
        newTemplate = templateRepository.save(newTemplate);

        // Clone criteria mappings
        List<TemplateCriteriaMapping> sourceMappings = mappingRepository.findByTemplate_TemplateId(sourceId);
        for (TemplateCriteriaMapping mapping : sourceMappings) {
            TemplateCriteriaMapping newMapping = new TemplateCriteriaMapping();
            newMapping.setTemplate(newTemplate);
            newMapping.setCriteria(mapping.getCriteria());
            newMapping.setMaxScore(mapping.getMaxScore());
            newMapping.setCriteriaOrder(mapping.getCriteriaOrder());
            mappingRepository.save(newMapping);
        }

        return templateMapper.toResponse(templateRepository.findById(newTemplate.getTemplateId()).get());
    }

    @Transactional
    public void deleteTemplate(Integer id) {
        if (!templateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Template not found with id: " + id);
        }
        templateRepository.deleteById(id);
    }

    @Transactional
    public AssessmentTemplateResponse addCriteriaToTemplate(Integer templateId, List<Integer> criteriaIds) {
        AssessmentTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + templateId));

        // Get current max order
        Integer currentMaxOrder = mappingRepository.findMaxOrderByTemplateId(templateId);
        int order = (currentMaxOrder != null) ? currentMaxOrder + 1 : 0;

        // Add new criteria
        for (Integer criteriaId : criteriaIds) {
            if (!mappingRepository.existsByTemplate_TemplateIdAndCriteria_CriteriaId(templateId, criteriaId)) {
                CriteriaBank criteria = criteriaRepository.findById(criteriaId)
                        .orElseThrow(() -> new ResourceNotFoundException("Criteria not found with id: " + criteriaId));

                TemplateCriteriaMapping mapping = new TemplateCriteriaMapping();
                mapping.setTemplate(template);
                mapping.setCriteria(criteria);
                mapping.setMaxScore(10); // Using default max score
                mapping.setCriteriaOrder(order++);
                mappingRepository.save(mapping);
            }
        }

        return templateMapper.toResponse(templateRepository.findById(templateId).get());
    }

    @Transactional
    public AssessmentTemplateResponse removeCriteriaFromTemplate(Integer templateId, Integer criteriaId) {
        if (!templateRepository.existsById(templateId)) {
            throw new ResourceNotFoundException("Template not found with id: " + templateId);
        }

        mappingRepository.deleteByTemplate_TemplateIdAndCriteria_CriteriaId(templateId, criteriaId);
        return templateMapper.toResponse(templateRepository.findById(templateId).get());
    }

    private void createTemplateCriteriaMappings(
            AssessmentTemplate template, 
            List<Integer> criteriaIds, 
            Integer defaultMaxScore) {
        List<TemplateCriteriaMapping> mappings = new ArrayList<>();
        int order = 0;
        for (Integer criteriaId : criteriaIds) {
            CriteriaBank criteria = criteriaRepository.findById(criteriaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Criteria not found with id: " + criteriaId));
            TemplateCriteriaMapping mapping = new TemplateCriteriaMapping();
            mapping.setTemplate(template);
            mapping.setCriteria(criteria);
            mapping.setMaxScore(defaultMaxScore);
            mapping.setCriteriaOrder(order++);
            mappings.add(mapping);
        }
        mappingRepository.saveAll(mappings);
    }
} 