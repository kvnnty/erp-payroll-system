package rw.gov.erp.v1.services.payroll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import rw.gov.erp.v1.dtos.requests.payroll.PayrollRequestDto;
import rw.gov.erp.v1.dtos.responses.payslip.PayslipResponseDto;
import rw.gov.erp.v1.entities.employee.Employee;

public interface PayrollService {

        public void generatePayroll(PayrollRequestDto request);

        public void approvePayroll(PayrollRequestDto request);

        public PayslipResponseDto generatePayslipForEmployee(Employee employee, Integer month, Integer year,
                        Map<String, BigDecimal> deductionRates);

        public List<PayslipResponseDto> getPayslipsByMonthAndYear(Integer month, Integer year);

        public PayslipResponseDto getEmployeePayslip(UUID employeeId, Integer month, Integer year);

        public List<PayslipResponseDto> getEmployeePayslips(UUID employeeId);

}
