package rw.gov.erp.v1.dtos.requests.deduction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeductionRequestDto {

  @NotBlank(message = "Deduction name (reason) is required")
  private String deductionName;

  @Positive(message = "Percentage for salary deduction is required")
  private BigDecimal percentage;
}
