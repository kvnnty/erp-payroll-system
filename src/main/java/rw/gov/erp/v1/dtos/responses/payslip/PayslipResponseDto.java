package rw.gov.erp.v1.dtos.responses.payslip;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayslipResponseDto {

  private UUID employeeId;

  private BigDecimal houseAmount;

  private BigDecimal transportAmount;

  private BigDecimal employeeTaxedAmount;

  private BigDecimal pensionAmount;

  private BigDecimal medicalInsuranceAmount;

  private BigDecimal otherTaxedAmount;

  private BigDecimal grossSalary;

  private BigDecimal netSalary;

  private BigDecimal baseSalary;

  private Integer month;

  private Integer year;
}
