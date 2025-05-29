package rw.gov.erp.v1.services.deduction.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rw.gov.erp.v1.dtos.requests.deduction.DeductionRequestDto;
import rw.gov.erp.v1.entities.deduction.Deduction;
import rw.gov.erp.v1.exceptions.DuplicateResourceException;
import rw.gov.erp.v1.exceptions.ResourceNotFoundException;
import rw.gov.erp.v1.repositories.payroll.DeductionRepository;
import rw.gov.erp.v1.services.deduction.DeductionService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeductionServiceImpl implements DeductionService {

  private final DeductionRepository deductionRepository;

  public List<Deduction> getAllDeductions() {
    return deductionRepository.findAll();
  }

  public Deduction getDeductionById(UUID id) {
    return deductionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Deduction not found with id: " + id));
  }

  public Deduction createDeduction(DeductionRequestDto request) {
    if (deductionRepository.existsByDeductionName(request.getDeductionName())) {
      throw new DuplicateResourceException("Deduction already exists: " + request.getDeductionName());
    }

    Deduction deduction = new Deduction();
    deduction.setCode("DED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    deduction.setDeductionName(request.getDeductionName());
    deduction.setPercentage(request.getPercentage());

    return deductionRepository.save(deduction);
  }

  public Deduction updateDeduction(UUID id, DeductionRequestDto request) {
    Deduction deduction = getDeductionById(id);

    if (!deduction.getDeductionName().equals(request.getDeductionName()) &&
        deductionRepository.existsByDeductionName(request.getDeductionName())) {
      throw new DuplicateResourceException("Deduction already exists: " + request.getDeductionName());
    }

    deduction.setDeductionName(request.getDeductionName());
    deduction.setPercentage(request.getPercentage());

    return deductionRepository.save(deduction);
  }

  public void deleteDeduction(UUID id) {
    Deduction deduction = getDeductionById(id);
    deductionRepository.delete(deduction);
  }
}
