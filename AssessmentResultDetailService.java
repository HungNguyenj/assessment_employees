package com.example.assessment_employees.service;

import com.example.assessment_employees.dto.response.AssessmentResultDetailRespone;
import com.example.assessment_employees.entity.AssessmentResult;
import com.example.assessment_employees.entity.AssessmentResultDetail;
import com.example.assessment_employees.entity.CriteriaBank;
import com.example.assessment_employees.repository.AssessmentResultDetailRepository;
import com.example.assessment_employees.repository.AssessmentResultRepository;
import com.example.assessment_employees.repository.CriteriaBankRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssessmentResultDetailService {

    private final AssessmentResultDetailRepository detailRepository;

    private final AssessmentResultRepository resultRepository;

    private final CriteriaBankRepository criteriaRepository;

    @Transactional
    //Get all AssessmentResultDetail
    public List<AssessmentResultDetail> getAllAssessmentResultDetail() {
        return detailRepository.findAll();
    }
    @Transactional
    //Get AssessmentResultDetail by id
    public AssessmentResultDetail getAssessmentResultDetailById(Integer id) {
        return detailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detail not found"));
    }
    @Transactional
    //Create AssessmentResultDetail
    public AssessmentResultDetail createAssessmentResultDetail(AssessmentResultDetailRespone dto) {
        AssessmentResultDetail detail = new AssessmentResultDetail();

        // Lấy thông tin Result và Criteria
        AssessmentResult result = resultRepository.findById(dto.getResult().getResultId())
                .orElseThrow(() -> new RuntimeException("Result not found"));
        CriteriaBank criteria = criteriaRepository.findById(dto.getCriteria().getCriteriaId())
                .orElseThrow(() -> new RuntimeException("Criteria not found"));

        // Set thông tin cho detail
        detail.setResult(result);
        detail.setCriteria(criteria);
        detail.setScore(dto.getScore());
        detail.setComments(dto.getComments());

        return detailRepository.save(detail);
    }

    @Transactional
    //Update AssessmentResultDetail
    public AssessmentResultDetail updateAssessmentResultDetail(Integer id, AssessmentResultDetailRespone dto) {
        Optional<AssessmentResultDetail> optionalDetail = detailRepository.findById(id);
        if (optionalDetail.isPresent()) {
            AssessmentResultDetail detail = optionalDetail.get();

            // Check if result and criteria are not null
            if (dto.getResult() == null || dto.getResult().getResultId() == null) {
                throw new RuntimeException("Result information is missing");
            }
            if (dto.getCriteria() == null || dto.getCriteria().getCriteriaId() == null) {
                throw new RuntimeException("Criteria information is missing");
            }
            // Cập nhật thông tin
            AssessmentResult result = resultRepository.findById(dto.getResult().getResultId())
                    .orElseThrow(() -> new RuntimeException("Result not found"));
            CriteriaBank criteria = criteriaRepository.findById(dto.getCriteria().getCriteriaId())
                    .orElseThrow(() -> new RuntimeException("Criteria not found"));

            detail.setResult(result);
            detail.setCriteria(criteria);
            detail.setScore(dto.getScore());
            detail.setComments(dto.getComments());

            return detailRepository.save(detail);
        } else {
            throw new RuntimeException("Detail not found");
        }
    }

    @Transactional
    //Delete AssessmentResultDetail
    public void deleteAssessmentResultDetail(Integer id) {
        Optional<AssessmentResultDetail> optionalDetail = detailRepository.findById(id);
        if (optionalDetail.isPresent()) {
            detailRepository.deleteById(id);
        } else {
            throw new RuntimeException("Detail not found");
        }
    }

}
