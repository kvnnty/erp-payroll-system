package rw.gov.erp.v1.services.payroll.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rw.gov.erp.v1.dtos.requests.payroll.PayrollRequestDto;
import rw.gov.erp.v1.entities.deduction.Deduction;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.entities.message.Message;
import rw.gov.erp.v1.entities.payroll.Payslip;
import rw.gov.erp.v1.enums.payslip.PayslipStatus;
import rw.gov.erp.v1.exceptions.DuplicateResourceException;
import rw.gov.erp.v1.exceptions.ResourceNotFoundException;
import rw.gov.erp.v1.repositories.message.MessageRepository;
import rw.gov.erp.v1.repositories.payroll.DeductionRepository;
import rw.gov.erp.v1.repositories.payroll.PayslipRepository;
import rw.gov.erp.v1.repositories.user.EmployeeRepository;
import rw.gov.erp.v1.services.mail.MailService;
import rw.gov.erp.v1.services.payroll.PayrollService;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollServiceImpl implements PayrollService {

  private final PayslipRepository payslipRepository;
  private final EmployeeRepository employeeRepository;
  private final DeductionRepository deductionRepository;
  private final MailService emailService;
  private final MessageRepository messageRepository;

  public List<Payslip> generatePayroll(PayrollRequestDto request) {
    List<Employee> activeEmployees = employeeRepository.findAllActiveEmployees();

    // Check for existing payroll for the month/year
    for (Employee employee : activeEmployees) {
      if (payslipRepository.existsByEmployeeIdAndMonthAndYear(
          employee.getId(), request.getMonth(), request.getYear())) {
        throw new DuplicateResourceException(
            "Payroll already exists for " + employee.getFirstName() + " " +
                employee.getLastName() + " for " + request.getMonth()
                + "/" + request.getYear());
      }
    }

    Map<String, BigDecimal> deductionRates = getDeductionRates();

    return activeEmployees.stream()
        .map(employee -> generatePayslipForEmployee(employee, request.getMonth(),
            request.getYear(),
            deductionRates))
        .collect(Collectors.toList());
  }

  public Payslip generatePayslipForEmployee(Employee employee, Integer month, Integer year,
      Map<String, BigDecimal> deductionRates) {
    BigDecimal baseSalary = employee.getEmployment().getBaseSalary();

    // Calculate allowances (added to gross salary)
    BigDecimal housingAmount = baseSalary.multiply(deductionRates.get("Housing")).divide(
        BigDecimal.valueOf(100), 2,
        RoundingMode.HALF_UP);
    BigDecimal transportAmount = baseSalary.multiply(deductionRates.get("Transport"))
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

    // Calculate gross salary
    BigDecimal grossSalary = baseSalary.add(housingAmount).add(transportAmount);

    // Calculate deductions (subtracted from gross salary)
    BigDecimal employeeTax = baseSalary.multiply(deductionRates.get("Employee Tax")).divide(
        BigDecimal.valueOf(100),
        2, RoundingMode.HALF_UP);
    BigDecimal pension = baseSalary.multiply(deductionRates.get("Pension")).divide(BigDecimal.valueOf(100),
        2,
        RoundingMode.HALF_UP);
    BigDecimal medicalInsurance = baseSalary.multiply(deductionRates.get("Medical Insurance"))
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    BigDecimal others = baseSalary.multiply(deductionRates.get("Others")).divide(BigDecimal.valueOf(100), 2,
        RoundingMode.HALF_UP);

    // Calculate net salary
    BigDecimal totalDeductions = employeeTax.add(pension).add(medicalInsurance).add(others);
    BigDecimal netSalary = grossSalary.subtract(totalDeductions);

    Payslip payslip = new Payslip();
    payslip.setEmployee(employee);
    payslip.setBaseSalary(baseSalary);
    payslip.setHouseAmount(housingAmount);
    payslip.setTransportAmount(transportAmount);
    payslip.setEmployeeTaxedAmount(employeeTax);
    payslip.setPensionAmount(pension);
    payslip.setMedicalInsuranceAmount(medicalInsurance);
    payslip.setOtherTaxedAmount(others);
    payslip.setGrossSalary(grossSalary);
    payslip.setNetSalary(netSalary);
    payslip.setMonth(month);
    payslip.setYear(year);
    payslip.setStatus(PayslipStatus.PENDING);

    return payslipRepository.save(payslip);
  }

  public Map<String, BigDecimal> getDeductionRates() {
    List<Deduction> deductions = deductionRepository.findAll();
    return deductions.stream()
        .collect(Collectors.toMap(Deduction::getDeductionName, Deduction::getPercentage));
  }

  public List<Payslip> getPayslipsByMonthAndYear(Integer month, Integer year) {
    return payslipRepository.findByMonthAndYear(month, year);
  }

  public Payslip getEmployeePayslip(UUID employeeId, Integer month, Integer year) {
    return payslipRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Payslip not found for employee " + employeeId + " for " + month + "/"
                + year));
  }

  public List<Payslip> getEmployeePayslips(UUID employeeId) {
    return payslipRepository.findByEmployeeIdOrderByYearDescMonthDesc(employeeId);
  }

  public List<Payslip> approvePayroll(Integer month, Integer year) {
    List<Payslip> payslips = payslipRepository.findByMonthAndYear(month, year);

    if (payslips.isEmpty()) {
      throw new ResourceNotFoundException("No payslips found for " + month + "/" + year);
    }

    payslips.forEach(payslip -> {
      payslip.setStatus(PayslipStatus.PAID);
      payslipRepository.save(payslip);

      String monthName = Month.of(payslip.getMonth()).name();
      String messageText = String.format(
          "Dear %s, your salary for %s/%d from RCA amounting to %s RWF has been credited to your account %s successfully.",
          payslip.getEmployee().getFirstName(),
          monthName,
          payslip.getYear(),
          payslip.getNetSalary(),
          payslip.getEmployee().getCode());

      Message message = new Message();
      message.setEmployee(payslip.getEmployee());
      message.setMessage(messageText);
      message.setMonthYear(payslip.getMonth() + "/" + payslip.getYear());

      messageRepository.save(message);

      // Send email notification
      emailService.sendMail(message.getEmployee().getEmail(), "Monthly Paycheck deposited", messageText);
    });

    return payslips;
  }
}
