package rw.gov.erp.v1.dtos.requests.employment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import rw.gov.erp.v1.enums.employment.EmploymentStatus;

@Data
public class EmploymentRequestDto {

  @NotNull(message = "Employment ID is required")
  private UUID employeeId;

  @NotBlank(message = "Department is required")
  private String department;

  @NotBlank(message = "Position is required")
  private String position;

  @Positive(message = "Base salary should be grater than zero")
  private BigDecimal baseSalary;

  private EmploymentStatus status = EmploymentStatus.ACTIVE;

  @FutureOrPresent(message = "Joining date can either be present or in the future")
  @NotNull(message = "Joining date is required")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate joiningDate;
}
