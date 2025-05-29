package rw.gov.erp.v1.dtos.responses.employment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.gov.erp.v1.enums.employment.EmploymentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentResponseDto {
  private UUID id;

  private String code;

  private UUID employeeId;

  private String department;

  private String position;

  private BigDecimal baseSalary;

  private EmploymentStatus status;

  private LocalDate joiningDate;
}
