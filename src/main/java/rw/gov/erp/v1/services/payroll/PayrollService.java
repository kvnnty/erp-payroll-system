package rw.gov.erp.v1.services.payroll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import rw.gov.erp.v1.dtos.requests.payroll.PayrollRequestDto;
import rw.gov.erp.v1.entities.employee.Employee;
import rw.gov.erp.v1.entities.payroll.Payslip;

public interface PayrollService {

        public List<Payslip> generatePayroll(PayrollRequestDto request);

        public Payslip generatePayslipForEmployee(Employee employee, Integer month, Integer year,
                        Map<String, BigDecimal> deductionRates);

        public Map<String, BigDecimal> getDeductionRates();

        public List<Payslip> getPayslipsByMonthAndYear(Integer month, Integer year);

        public Payslip getEmployeePayslip(UUID employeeId, Integer month, Integer year);

        public List<Payslip> getEmployeePayslips(UUID employeeId);

        public List<Payslip> approvePayroll(Integer month, Integer year);
}
