package com.example.assessment_employees.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.assessment_employees.dto.request.CriteriaRequest;
import com.example.assessment_employees.dto.response.CriteriaResponse;
import com.example.assessment_employees.entity.CriteriaBank;
import com.example.assessment_employees.exception.ResourceNotFoundException;
import com.example.assessment_employees.mapper.CriteriaMapper;
import com.example.assessment_employees.repository.CriteriaBankRepository;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriteriaBankService {
    private final CriteriaBankRepository criteriaRepository;
    private final CriteriaMapper criteriaMapper;

    public List<CriteriaResponse> getAllCriteria() {
        return criteriaRepository.findAll().stream()
                .map(criteriaMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CriteriaResponse getCriteriaById(Integer id) {
        CriteriaBank criteria = criteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criteria not found with id: " + id));
        return criteriaMapper.toResponse(criteria);
    }

    @Transactional
    public CriteriaResponse createCriteria(CriteriaRequest criteriaRequest) {
        if (criteriaRepository.existsCriteriaBankByCriteriaName(criteriaRequest.getCriteriaName())) {
            throw new EntityExistsException("Criteria name already exists");
        }

        CriteriaBank criteria = criteriaMapper.toEntity(criteriaRequest);
        criteria = criteriaRepository.save(criteria);
        return criteriaMapper.toResponse(criteria);
    }

    @Transactional
    public CriteriaResponse updateCriteria(Integer id, CriteriaRequest criteriaRequest) {
        CriteriaBank existingCriteria = criteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criteria not found with id: " + id));

        if (!existingCriteria.getCriteriaName().equals(criteriaRequest.getCriteriaName()) 
            && criteriaRepository.existsCriteriaBankByCriteriaName(criteriaRequest.getCriteriaName())) {
            throw new EntityExistsException("Criteria name already exists");
        }

        existingCriteria.setCriteriaName(criteriaRequest.getCriteriaName());
        existingCriteria.setCategory(criteriaRequest.getCategory());
        existingCriteria.setDescription(criteriaRequest.getDescription());
        existingCriteria.setDefaultMaxScore(criteriaRequest.getDefaultMaxScore());
        existingCriteria.setIsActive(criteriaRequest.getIsActive());

        existingCriteria = criteriaRepository.save(existingCriteria);
        return criteriaMapper.toResponse(existingCriteria);
    }

    @Transactional
    public void deleteCriteria(Integer id) {
        if (!criteriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Criteria not found with id: " + id);
        }
        criteriaRepository.deleteById(id);
    }
} 