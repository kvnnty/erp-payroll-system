package rw.gov.erp.v1.entities.payroll;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rw.gov.erp.v1.audits.Auditable;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.enums.payslip.PayslipStatus;

@Entity
@Table(name = "payslips", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "employee_id", "month", "year" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payslip extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  @Column(name = "housing", precision = 15, scale = 2)
  private BigDecimal houseAmount;

  @Column(name = "transport", precision = 15, scale = 2)
  private BigDecimal transportAmount;

  @Column(name = "tax", precision = 15, scale = 2)
  private BigDecimal employeeTaxedAmount;

  @Column(name = "pension", precision = 15, scale = 2)
  private BigDecimal pensionAmount;

  @Column(name = "medical", precision = 15, scale = 2)
  private BigDecimal medicalInsuranceAmount;

  @Column(name = "others", precision = 15, scale = 2)
  private BigDecimal otherTaxedAmount;

  @Column(name = "gross_salary", precision = 15, scale = 2)
  private BigDecimal grossSalary;

  @Column(name = "net_salary", precision = 15, scale = 2)
  private BigDecimal netSalary;

  @Column(name = "base_salary", precision = 15, scale = 2)
  private BigDecimal baseSalary;

  private Integer month;
  private Integer year;

  @Enumerated(EnumType.STRING)
  private PayslipStatus status = PayslipStatus.PENDING;

}
