package rw.gov.erp.v1.services.deduction;

import java.util.List;
import java.util.UUID;

import rw.gov.erp.v1.dtos.requests.deduction.DeductionRequestDto;
import rw.gov.erp.v1.entities.deduction.Deduction;

public interface DeductionService {

  public List<Deduction> getAllDeductions();

  public Deduction getDeductionById(UUID id);

  public Deduction createDeduction(DeductionRequestDto request);

  public Deduction updateDeduction(UUID id, DeductionRequestDto request);

  public void deleteDeduction(UUID id);
}
