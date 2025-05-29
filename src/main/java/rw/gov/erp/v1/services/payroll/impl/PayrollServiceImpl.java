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
import rw.gov.erp.v1.exceptions.BadRequestException;
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

  public void generatePayroll(PayrollRequestDto request) {
    List<Employee> activeEmployees = employeeRepository.findAllActiveEmployees();

    // Check for existing payroll
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

    activeEmployees.stream()
        .map(employee -> generatePayslipForEmployee(employee, request.getMonth(), request.getYear(), deductionRates))
        .collect(Collectors.toList());
  }

  public Payslip generatePayslipForEmployee(Employee employee, Integer month, Integer year,
      Map<String, BigDecimal> deductionRates) {

    BigDecimal baseSalary = employee.getEmployment().getBaseSalary();
    double base = baseSalary.doubleValue();

    double housingRate = getRate(deductionRates, "Housing");
    double transportRate = getRate(deductionRates, "Transport");
    double taxRate = getRate(deductionRates, "Employee Tax");
    double pensionRate = getRate(deductionRates, "Pension");
    double insuranceRate = getRate(deductionRates, "Medical Insurance");
    double otherRate = getRate(deductionRates, "Others");

    // Allowances
    BigDecimal housingAmount = toBigDecimal(housingRate * base / 100);
    BigDecimal transportAmount = toBigDecimal(transportRate * base / 100);

    // Gross
    BigDecimal grossSalary = baseSalary.add(housingAmount).add(transportAmount);

    // Deductions
    BigDecimal employeeTax = toBigDecimal(taxRate * base / 100);
    BigDecimal pension = toBigDecimal(pensionRate * base / 100);
    BigDecimal medicalInsurance = toBigDecimal(insuranceRate * base / 100);
    BigDecimal others = toBigDecimal(otherRate * base / 100);

    BigDecimal totalDeductions = employeeTax.add(pension).add(medicalInsurance).add(others);

    // Safety check
    if (totalDeductions.compareTo(grossSalary) > 0) {
      throw new IllegalStateException("Deductions exceed gross salary for employee " + employee.getCode());
    }

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

  private double getRate(Map<String, BigDecimal> map, String key) {
    BigDecimal value = map.get(key);
    if (value == null) {
      throw new IllegalStateException("Missing deduction rate for key: " + key);
    }
    return value.doubleValue();
  }

  private BigDecimal toBigDecimal(double value) {
    return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
  }

  private Map<String, BigDecimal> getDeductionRates() {
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
            "Payslip not found for employee " + employeeId + " for " + month + "/" + year));
  }

  public List<Payslip> getEmployeePayslips(UUID employeeId) {
    return payslipRepository.findByEmployeeIdOrderByYearDescMonthDesc(employeeId);
  }

  public void approvePayroll(PayrollRequestDto request) {
    final Integer month = request.getMonth();
    final Integer year = request.getYear();

    List<Payslip> payslips = payslipRepository.findByMonthAndYear(month, year);

    if (payslips.isEmpty()) {
      throw new ResourceNotFoundException("No payslips found for " + month + "/" + year);
    }

    List<Payslip> unpaidPayslips = payslips.stream()
        .filter(p -> p.getStatus() != PayslipStatus.PAID)
        .collect(Collectors.toList());

    if (unpaidPayslips.isEmpty()) {
      throw new BadRequestException("All payslips for " + month + "/" + year + " are already paid.");
    }

    unpaidPayslips.forEach(payslip -> {
      payslip.setStatus(PayslipStatus.PAID);
      payslipRepository.save(payslip);

      String monthName = Month.of(payslip.getMonth()).name();
      String messageText = String.format(
          "Dear %s, your salary for %s/%d amounting to %s RWF has been credited to your account %s successfully.",
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
      emailService.sendMail(message.getEmployee().getEmail(), "Monthly Paycheck Notification", messageText);
    });
  }

}
