package rw.gov.erp.v1.entities.employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.gov.erp.v1.audits.Auditable;
import rw.gov.erp.v1.enums.employment.EmploymentStatus;

@Entity
@Table(name = "employments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employment extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(unique = true, nullable = false)
  private String code;

  @OneToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  @NotNull
  private String department;

  @NotNull
  private String position;

  @Positive
  @Column(name = "base_salary", precision = 15, scale = 2)
  private BigDecimal baseSalary;

  @Enumerated(EnumType.STRING)
  private EmploymentStatus status = EmploymentStatus.ACTIVE;

  @Column(name = "joining_date")
  private LocalDate joiningDate;

}
